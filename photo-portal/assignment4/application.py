from flask import Flask, redirect, request, url_for
from flask import Response

import requests
import secrets

import flask
from flask import request
from flask import Flask, render_template

from jinja2 import Template

from flask import session

from werkzeug.utils import secure_filename

import logging
from logging.config import dictConfig

import os
import json

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm import declarative_base, relationship
from sqlalchemy import Column, Integer, ForeignKey, String

import bcrypt

import google.oauth2.credentials
import google_auth_oauthlib.flow
import googleapiclient.discovery

# This variable specifies the name of a file that contains the OAuth 2.0
# information for this application, including its client_id and client_secret.
CLIENT_SECRETS_FILE = "google_oauth_client_secret.json"



# This OAuth 2.0 access scope allows for full read/write access to the
# authenticated user's account and requires requests to use an SSL connection.
SCOPES = ['https://www.googleapis.com/auth/userinfo.email']
API_SERVICE_NAME = 'oauth2'
API_VERSION = 'v2'
os.environ['OAUTHLIB_RELAX_TOKEN_SCOPE'] = '1'


dictConfig({
    'version': 1,
    'formatters': {'default': {
        'format': '[%(asctime)s] %(levelname)s in %(module)s: %(message)s',
    }},
    'handlers': {'wsgi': {
        'class': 'logging.StreamHandler',
        'stream': 'ext://flask.logging.wsgi_errors_stream',
        'formatter': 'default'
    },
     'file.handler': {
            'class': 'logging.handlers.RotatingFileHandler',
            'filename': 'photoportal.log',
            'maxBytes': 10000000,
            'backupCount': 5,
            'level': 'INFO',
        },
    },
    'root': {
        'level': 'INFO',
        'handlers': ['file.handler']
    }
})

PHOTO_FOLDER = "static/images/photos"


app = Flask(__name__)

app.secret_key = secrets.token_hex()

# Ref: https://stackoverflow.com/questions/14888799/disable-console-messages-in-flask-server
log = logging.getLogger('werkzeug')
log.setLevel(logging.ERROR)

# SQLite Database creation
Base = declarative_base()

db_name = os.getenv("PHOTO_PORTAL_DB","photosdata.db")
db_url = "sqlite:///" + db_name
engine = create_engine(db_url, future=True, connect_args={'check_same_thread': False})

DBSession = sessionmaker(bind=engine)

# Class that represents Database table "PhotoData"
class PhotoData(Base):
    __tablename__ = 'photos'
    id = Column(Integer, primary_key=True)
    name = Column(String)
    date_taken = Column(String)
    tags = Column(String)

    def __repr__(self):
        return "<PhotoData(name='%s', date_taken='%s', tags='%s')>" % (
                self.name, self.date_taken, self.tags)

    # Ref: https://stackoverflow.com/questions/5022066/how-to-serialize-sqlalchemy-result-to-json
    def as_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.columns}


# Requirement 1: Add a User class to represent User table.
# Table name should be 'user'
# In the as_dict method, skip the password field/attribute when returning the dict.


# Class that represents in-memory representation of a photo
class Photo():
	def __init__(self, photo_name='', date_taken='', tags=''):
		self.name = photo_name
		self.date_taken = date_taken
		self.tags = tags


Base.metadata.create_all(engine)


# This method queries photos from the database and loads them in as in-memory list.
def load_photos():
    session = DBSession()
    photo_list = session.query(PhotoData)
    photos = []
    for photo in photo_list:
        found = False
        for p in photos:
            if p.name == photo.name:
                found = True

        if not found:
            photo = Photo(photo_name=photo.name, 
                          date_taken=photo.date_taken,
                          tags=photo.tags)
            photos.append(photo)

    return photos


# https://stackoverflow.com/questions/19473250/how-to-get-user-email-after-oauth-with-google-api-python-client
def get_user_info(credentials):
    user_info_service = googleapiclient.discovery.build(API_SERVICE_NAME, API_VERSION, credentials=credentials)
    user_info = None
    try:
        user_info = user_info_service.userinfo().get().execute()
    except errors.HttpError as e:
        logging.error('An error occurred: %s', e)
    if user_info and user_info.get('id'):
        return user_info
    else:
        raise Exception()


def credentials_to_dict(credentials):
  return {'token': credentials.token,
          'refresh_token': credentials.refresh_token,
          'token_uri': credentials.token_uri,
          'client_id': credentials.client_id,
          'client_secret': credentials.client_secret,
          'scopes': credentials.scopes}



## Requirement 2.1: Add a POST REST API to add users
## Requirement 2.1.1: Make sure that User with name does not exists in the DB
## Requirement 2.2: Use bcrypt to hash the password.

## POST /users REST API
@app.route("/users", methods=['POST'])
def add_user():
    app.logger.info("Inside add_user")
    data = request.json
    app.logger.info("Received request:%s", str(data))

    name = data['name']
    password = data['password']
    user_type = data["user_type"]

    session = DBSession()
    user = session.query(User).filter_by(name=name).first()
    if user == None:
        hashed = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())
        newuser = User(name=name, password=hashed, user_type=user_type)
        session.add(newuser)
        session.commit()
        return newuser.as_dict()
    else:
        status = ("User with name {name} already exists.\n").format(name=name)
        return Response(status, status=400)


## Requirement 2.3: Add a GET REST API to get users


# REST API to get photos - Sends photo names back
@app.route("/photos", methods=["GET"])
def get_photos():

    photos = load_photos()
    photo_json_list = []
    for photo in photos:
        photo_json_list.append(json.dumps(photo.__dict__))

    ret_obj = {}
    ret_obj["photos"] = photo_json_list
    return ret_obj


@app.route("/photos/<photo>/tags")
def get_tags_for_photo(photo):
    app.logger.info("Inside get_tags_for_photo:" + photo)

    tags_to_return = ""
    dbsession = DBSession()
    photo_obj = dbsession.query(PhotoData).filter_by(name=photo)
    if photo_obj.first() != None:
        photo_obj1 = photo_obj.first()
        tags_to_return = photo_obj1.tags
        app.logger.info("Tags to return:" + tags_to_return)

    ret_obj = {}
    ret_obj["tags"] = tags_to_return
    return ret_obj


@app.route("/update_tags", methods=["POST"])
def update_tags():
    app.logger.info("Inside update_tags")

    photo_name = request.form["photo_name_for_tags_update"].strip()
    app.logger.info("Photo:" + photo_name)

    new_tags = request.form["new_tags"].strip()
    app.logger.info("New tags:" + new_tags)

    dbsession = DBSession()
    photo = dbsession.query(PhotoData).filter_by(name=photo_name).first()
    photo.tags = new_tags # new_tags should be overwritten - this is okay as we are first retrieving existing tags;
    dbsession.commit()

    username = ""
    if "username" in session:
        username = session['username']

    upload_form_display="display:block;"
    slide_show_display="display:block;" 

    photos = load_photos()

    tag_update_status = "Tags successfully updated for:" + photo_name

    return render_template("photo-portal.html",
                            username=username,
                            upload_form_display=upload_form_display,
                            slide_show_display=slide_show_display,
                            photo_upload_status=tag_update_status,
                            photo_list=photos,
                            user="admin")


@app.route("/delete_photos", methods=["POST"])
def delete_photos():
    app.logger.info("Inside delete_photos.")
    dbsession = DBSession()
    selected_photos = request.form.getlist("photos_to_update_delete")
    final_status = ""
    for name in selected_photos:
        app.logger.info("Photo to delete:" + name)
        photo = dbsession.query(PhotoData).filter_by(name=name).first()

        if photo == None:
            status = ("Photo with name {name} not found.\n").format(name=name)
        else:
            app.logger.info("Found Photo:%s\n", str(photo))
            # Delete from the filesystem
            file_path = PHOTO_FOLDER + "/" + name
            if os.path.exists(file_path):
                os.remove(file_path)

            # Delete from the DB
            dbsession.delete(photo)
            dbsession.commit()
            status = ("Photo with name {name} deleted.\n").format(name=name)
            final_status = final_status + status

    username = ""
    if "username" in session:
        username = session['username']

    upload_form_display="display:block;"
    slide_show_display="display:block;" 

    photos = load_photos()

    return render_template("photo-portal.html",
                            username=username,
                            upload_form_display=upload_form_display,
                            slide_show_display=slide_show_display,
                            photo_upload_status=final_status,
                            photo_list=photos,
                            user="admin")



@app.route("/search", methods=["POST"])
def search():
	app.logger.info("Inside search")

	photos = load_photos()

	search_criteria = request.form["search_select"].strip()
	app.logger.info("Search criteria:" + search_criteria)

	search_data = request.form["search_input"].strip()
	app.logger.info("Search data:" + search_data)

	username = ""
	if "username" in session:
		username = session['username']

	matching_photos = []
	for photo in photos:
		if search_criteria == "Name":
			if search_data in photo.name:
				matching_photos.append(photo)
		if search_criteria == "Date":
			if search_data == photo.date_taken:
				matching_photos.append(photo)
		if search_criteria == "Tags":
			if search_data in photo.tags:
				matching_photos.append(photo)


	app.logger.info("Matching photos:")
	app.logger.info(matching_photos)

	if len(matching_photos) > 0:
		status = "Matching photos found."
	else:
		status = "No matching photos found."

	upload_form_display="display:block;"
	slide_show_display="display:none;"
	type_of_user = "admin"
	if "@gmail.com" in username:
		upload_form_display="display:none;"
		slide_show_display="display:block;" 
		type_of_user = "general"

	return render_template("photo-portal.html",
							username=username,
							upload_form_display=upload_form_display,
							slide_show_display=slide_show_display,
							photo_upload_status=status,
							photo_list=matching_photos,
							user=type_of_user)


@app.route("/upload", methods=["POST"])
def upload_photo():
	app.logger.info("Inside upload_photo")

	username = ""
	if "username" in flask.session:
		username = flask.session['username']

	date_taken = request.form["date_taken"].strip()
	tags = request.form["tags"].strip()
	file = request.files['photo_name']
	filename = secure_filename(file.filename)
	filename = filename.strip()
	app.logger.info("File Name:%s\n", filename)
	app.logger.info("Date taken:" + date_taken)
	app.logger.info("Tags:" + tags)

	if not os.path.exists(PHOTO_FOLDER):
		os.makedirs(PHOTO_FOLDER)

	file.save(PHOTO_FOLDER + "/" + filename)

	photo_data = PhotoData(name=filename, date_taken=date_taken, tags=tags)

    # First query to find out if a photo with the name already exists.
    # Commit the photo only if photo with the name does not exist.
	dbsession = DBSession()
	photo_obj = dbsession.query(PhotoData).filter_by(name=filename).first()
	if photo_obj == None:
		app.logger.info("Found Photo:%s\n", str(photo_obj))
		status = "Photo " + filename + " uploaded successfully."
		dbsession.add(photo_data)
		dbsession.commit()
	else:
		status = "Photo " + filename + " already exists."

	photos = load_photos()

	slide_show_display = "display:block"

	return render_template("photo-portal.html",
							username=username,
							photo_upload_status=status,
							slide_show_display=slide_show_display,
							photo_list=photos,
                            user="admin")


# Requirement 3.2
# - Connect the "login_with_google" form with this method
# - For http methods list in the definition, use POST and GET
def authorize():
    if not os.path.exists(CLIENT_SECRETS_FILE):
        return render_template('google-oauth-client-secrets-file-missing.html')

    # Create flow instance to manage the OAuth 2.0 Authorization Grant Flow steps.
    flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(CLIENT_SECRETS_FILE, scopes=SCOPES)

    # The URI created here must exactly match one of the authorized redirect URIs
    # for the OAuth 2.0 client, which you configured in the API Console. If this
    # value doesn't match an authorized URI, you will get a 'redirect_uri_mismatch'
    # error.
    redirect_uri = flask.url_for('oauth2callback', _external=True)
    parts = redirect_uri.split("/oauth2callback")
    print("Parts:" + str(parts))
    if ":5009" not in redirect_uri:
        flow.redirect_uri = parts[0] + ":5009/oauth2callback"
    else:
        flow.redirect_uri = redirect_uri
    print("Redirect URI:" + flow.redirect_uri)

    authorization_url, state = flow.authorization_url(
        # Enable offline access so that you can refresh an access token without
        # re-prompting the user for permission. Recommended for web server apps.
        access_type='offline',
        # Enable incremental authorization. Recommended as a best practice.
        include_granted_scopes='true')

    # Store the state so the callback can verify the auth server response.
    flask.session['state'] = state
    print("Authorization URL:" + authorization_url)

    return flask.redirect(authorization_url)


@app.route('/oauth2callback')
def oauth2callback():
    # Specify the state when creating the flow in the callback so that it can
    # verified in the authorization server response.
    state = flask.session['state']

    flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(CLIENT_SECRETS_FILE, scopes=SCOPES, state=state)
    flow.redirect_uri = flask.url_for('oauth2callback', _external=True)

    # Use the authorization server's response to fetch the OAuth 2.0 tokens.
    authorization_response = flask.request.url
    print("Authorization response:" + authorization_response)
    flow.fetch_token(authorization_response=authorization_response)

    # Store credentials in the session.
    # In a production app, you likely want to save these
    # credentials in a persistent database instead.
    credentials = flow.credentials
    flask.session['credentials'] = credentials_to_dict(credentials)

    return flask.redirect(flask.url_for('login'))


@app.route("/logout",methods=['POST'])
def logout():
    app.logger.info("Logout called.")
    app.logger.info("Before returning...")
    user = flask.session["username"]
    flask.session.pop('username', None)

    # delete credentials from Flask session
    if 'credentials' in flask.session:
        credentials = google.oauth2.credentials.Credentials(**flask.session['credentials'])

        revoke = requests.post('https://oauth2.googleapis.com/revoke',
                params={'token': credentials.token},
                headers = {'content-type': 'application/x-www-form-urlencoded'})

        status_code = getattr(revoke, 'status_code')
        if status_code == 200:
            print("Credentials successfully revoked.")
        del flask.session['credentials']

    if "@gmail.com" in user:
    	return flask.redirect("/")
    else:
        return flask.redirect("/admin")


@app.route("/login", methods=['POST','GET'])
def login():
    username = ""
    password = ""
    gmail = ""
    user = ""
    upload_form_display = ""
    status = ""

    photos = load_photos()

    if 'username' in request.form:
        type_of_user = "admin"
        username = request.form['username'].strip()
        password = request.form['password'].strip()
        user = username
        app.logger.info("Username:%s", username)
        app.logger.info("Password:%s", password)
        upload_form_display = "display:block;"
        slide_show_display = "display:block;"

        # Requirement 2.4
        # Requirement 2.5

        
    # Load credentials from the session.
    if 'credentials' in flask.session:
        type_of_user = "general"
        credentials = google.oauth2.credentials.Credentials(**flask.session['credentials'])

        user_email = get_user_info(credentials)['email']
        print("User email:" + user_email)
        user = user_email

        # Requirement 3.3
        # - Insert User in the DB
        # - Leave password empty


        flask.session['credentials'] = credentials_to_dict(credentials)
        upload_form_display = "display:none;"
        slide_show_display = "display:block;"

    app.logger.info("User photos:")
    app.logger.info(photos)
    app.logger.info("---")

    flask.session["username"] = user

    return render_template('photo-portal.html',
    						upload_form_display=upload_form_display,
    						username=user,
    						slide_show_display=slide_show_display,
    						photo_list=photos,
                            user=type_of_user)


@app.route("/admin")
def adminindex():
    return render_template('adminindex.html')


@app.route("/")
def index():
    return render_template('index.html')


if __name__ == "__main__":

    app.debug = False
    app.logger.info('Portal started...')
    app.run(host='0.0.0.0', port=5009, ssl_context='adhoc')
