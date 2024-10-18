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

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm import declarative_base, relationship
from sqlalchemy import Column, Integer, ForeignKey, String


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

app.logger.setLevel(logging.CRITICAL)

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


# Class that represents in-memory representation of a photo
class Photo:
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


# REST API to get photos - Sends photo names back
@app.route("/photos", methods=["GET"])
def get_photos():

    photos = load_photos()
    photo_names = []
    for photo in photos:
        photo_names.append(photo.name)

    ret_obj = {}
    ret_obj["photos"] = photo_names
    return ret_obj


# Requirement 2.2
@app.route("/photos/<photo>/tags")
def get_tags_for_photo(photo):
    app.logger.info("Inside get_tags_for_photo:" + photo)

    tags_to_return = ""

    # Query Database for photo and return the tags


    ret_obj = {}
    ret_obj["tags"] = tags_to_return
    return ret_obj


# Requirement 3.3
@app.route("/update_tags", methods=["POST"])
def update_tags():
    app.logger.info("Inside update_tags")


    photos = load_photos()

    return render_template("photo-portal.html",
                            photo_list=photos)


# Requirement 3.4
@app.route("/delete_photos", methods=["POST"])
def delete_photos():
    app.logger.info("Inside delete_photos.")

    photos = load_photos()

    return render_template("photo-portal.html",
                            photo_list=photos
                            )



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

	# Requirement 1: Use SQLAlchemy to store the photo
	# First query to find out if a photo with the name already exists.
	# Commit the photo only if photo with the name does not exist.



	photos = load_photos()

	slide_show_display = "display:block"

	return render_template("photo-portal.html",
							username=username,
							photo_upload_status=status,
							slide_show_display=slide_show_display,
							photo_list=photos,
                            user="admin")


@app.route("/logout",methods=['POST'])
def logout():
    app.logger.info("Logout called.")
    app.logger.info("Before returning...")
    user = flask.session["username"]
    flask.session.pop('username', None)

    if "@gmail.com" in user:
    	return flask.redirect("/")
    else:
        return flask.redirect("/admin")


@app.route("/login", methods=['POST'])
def login():
    username = ""
    password = ""
    gmail = ""
    user = ""
    upload_form_display = ""
    status = ""

    photos = load_photos()

    type_of_user = "admin"
    if 'username' in request.form:
        username = request.form['username'].strip()
        password = request.form['password'].strip()
        user = username
        app.logger.info("Username:%s", username)
        app.logger.info("Password:%s", password)
        upload_form_display = "display:block;"
        slide_show_display = "display:block;"
    if 'gmail' in request.form:
        gmail = request.form['gmail'].strip()
        user = gmail
        type_of_user = "general"
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
    app.run(host='0.0.0.0', port=5009)
