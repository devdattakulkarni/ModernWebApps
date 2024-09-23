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

from logging.config import dictConfig

import os

app = Flask(__name__)

app.secret_key = secrets.token_hex()


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
            'level': 'DEBUG',
        },
    },
    'root': {
        'level': 'INFO',
        'handlers': ['file.handler']
    }
})

PHOTO_FOLDER = "static/images/photos"

# In-memory photo storage.
photos = []


class Photo:
	def __init__(self, photo_name='', date_taken='', tags=''):
		self.name = photo_name
		self.date_taken = date_taken
		self.tags = tags

# Requirement 5
# 5.1 Define the method that will handle the search_form submission
# 5.3 Implement search of photos based the input criteria and the provided data
# 5.4 Create the matching photos list and set the appropriate status message when rendering
#     the photo-portal.html
# 5.5 Set empty photo list and appropriate status message when rendering the photo-portal.html
# Pass the username by extracting it from flask's session dictionary.

@app.route("/upload", methods=["POST"])
def upload_photo():
    app.logger.info("Inside upload_photo")

    username = ""
    if "username" in session:
        username = session['username']

    date_taken = request.form["date_taken"].strip()
    tags = request.form["tags"].strip()
    file = request.files['photo_name']
    filename = secure_filename(file.filename)
    filename = filename.strip()
    app.logger.info("File Name:%s\n", filename)

    if not os.path.exists(PHOTO_FOLDER):
        os.makedirs(PHOTO_FOLDER)

    file.save(PHOTO_FOLDER + "/" + filename)

    photo = Photo(photo_name=filename, date_taken=date_taken, tags=tags)
    photos.append(photo)

    app.logger.info("Date taken:" + date_taken)
    app.logger.info("Tags:" + tags)

    # Requirement 3
    # 3.1 - Set the status and pass that in to photo_upload_status parameter.

    return render_template("photo-portal.html", username=username, photo_list=photos)


@app.route("/logout",methods=['POST'])
def logout():
    app.logger.info("Logout called.")
    app.logger.info("Before returning...")
    user = flask.session["username"]
    flask.session.pop('username', None)

    # Requirement 2.1 and 2.2
    # Use the "user" variable above to determine which endpoint to redirect to
    # Use assumption 1 to determine whether the user is admin user or general user

    return flask.redirect("/admin")


@app.route("/adminlogin", methods=['POST'])
def adminlogin():
    username = ""
    password = ""
    gmail = ""
    user = ""
    upload_form_display = ""
    status = ""
    if 'username' in request.form:
        username = request.form['username'].strip()
        password = request.form['password'].strip()
        user = username
        app.logger.info("Username:%s", username)
        app.logger.info("Password:%s", password)
        upload_form_display = "display:block;"
    app.logger.info("User photos:")
    app.logger.info(photos)
    app.logger.info("---")

    flask.session["username"] = user

    return render_template('photo-portal.html',
    						upload_form_display=upload_form_display,
    						username=user,
    						photo_upload_status=status,
    						photo_list=photos)


# Requirement 1.2 and 1.3
# 1.2 Add method that handles the general user login form submission
# Refer to adminlogin method above
# 1.3.1 Set the status as per the requirements and pass it in the render_template
# 1.3.2 Set the upload_form_display status appropriately so that the photo upload form is not visible. 
# 1.4 Save the logged in user's name in flask session and pass that in the render_template for username parameter.


@app.route("/admin")
def adminindex():
    return render_template('adminindex.html')


# Requirement 1.1
# Render index.html for the route "/" (refer to adminindex method above)
# You need to add index.html - refer to adminindex.html
# Add only one input box in index.html (see requirement 1.1 in assignment description)



if __name__ == "__main__":

    app.debug = False
    app.logger.info('Portal started...')
    app.run(host='0.0.0.0', port=5009)
