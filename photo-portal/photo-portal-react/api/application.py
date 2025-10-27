from flask import Flask, request, jsonify
from flask_cors import CORS

import flask
from flask import session

import secrets
import os

app = Flask(__name__)
CORS(app, supports_credentials=True)

app.secret_key = secrets.token_hex() 

UPLOAD_FOLDER = 'static/images/photos'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)


# Requirement 1.1
# Add SQLAlchemy object to represent Admin User (refer assignment3)

# Requirement 2.1
# Add SQLAlchemy object to represent General User (refer assignment3)

# Requirement 3.1
# Add SQLAlchemy object to store photos; Add a column to store photo comments 


# Requirement 3.4
@app.route("/api/photos/<photo_name>/comments", methods=["GET"])
def get_photo_comments(photo_name):
    print("Comments for:" + photo_name)
    comments = "This is comment 1"
    return jsonify({"comments": comments})


# Requirement 3.3: Query the database to return photo metadata (date_taken and tags) for all photos
@app.route("/api/photos", methods=["GET"])
def get_photos():
    photos = [{"name": p, "date_taken": "...", "tags": "..."} for p in os.listdir("static/images/photos")]
    return jsonify({"photos": photos})


# Requirement 5 -- Add search functionality - Refer to your assignment2 implementation

@app.route('/api/upload', methods=['POST'])
def upload_photo():
    file = request.files.get('photo_file')
    date_taken = request.form.get('date_taken')
    tags = request.form.get('tags')

    # Requirement 3.2

    if not file:
        return jsonify({'error': 'No file uploaded'}), 400

    save_path = os.path.join(UPLOAD_FOLDER, file.filename)
    file.save(save_path)

    # You’d normally save metadata to DB here
    return jsonify({
        'name': file.filename,
        'date_taken': date_taken,
        'tags': tags
    })

# -- Admin user -- 

# Requirement 1.2 - Add a POST call to register an admin user

# Requirement 1.3 - Add a GET call to get all registered admin users

# Requirement 1.4 - Complete the adminlogin implementation
@app.route("/api/adminlogin", methods=["POST"])
def admin_login():
    data = request.get_json()
    print("Received:", data)
    username = data.get("name")
    password = data.get("password")
    # Query database to check if username/password exists or  not
    # Replace the below if check with database query
    if username == "admin" and password == "admin":
        flask.session["username"] = username
        return jsonify({"message": "Success", "username": username})
    else:
        return jsonify({"message": "NotFound", "username": username})

# -- General user --

# Requirement 2.2 - Add a POST call to register a general user

# Requirement 2.3 - Add a GET call to get all registered general users

# Requirement 2.4 - Complete the generaluserlogin implementation
@app.route("/api/generaluserlogin", methods=["POST"])
def generaluser_login():
    data = request.get_json()
    print("Received:", data)
    username = data.get("gmail")
    flask.session["username"] = username
    # Query database to check if username/password exists or  not
    # Replace the below if check with database query
    # If the generaluser is not present, return NotFound
    return jsonify({"message": "Success", "username": username})

# Requirement 2.3



@app.route("/api/logout",methods=['POST'])
def logout():
    data = request.get_json()
    print("Received:", data)
    username = data.get("userName")
    user_type = "admin"
    if "gmail.com" in username:
        user_type = "general"

    flask.session.clear()
    return jsonify({"message": "Success", "usertype": user_type})


if __name__ == "__main__":

    app.debug = False
    #app.logger.info('Portal started...')
    app.run(host='0.0.0.0', port=5000)
