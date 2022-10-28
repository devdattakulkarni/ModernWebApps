# -*- coding: utf-8 -*-

# Ref: https://developers.google.com/identity/protocols/oauth2/web-server#authorization-errors-redirect-uri-mismatch
# Modified from the original
from flask import Flask, render_template, redirect, request, url_for, abort, jsonify
from jinja2 import Environment, PackageLoader, select_autoescape
from flask import Response

import requests
import string

import os
import time
import random

from jinja2 import Template


import os
import flask
import requests

import google.oauth2.credentials
import google_auth_oauthlib.flow
import googleapiclient.discovery

# This variable specifies the name of a file that contains the OAuth 2.0
# information for this application, including its client_id and client_secret.
CLIENT_SECRETS_FILE = "client_secret.json"

# This OAuth 2.0 access scope allows for full read/write access to the
# authenticated user's account and requires requests to use an SSL connection.
#SCOPES = ['https://www.googleapis.com/auth/drive.metadata.readonly']
SCOPES = ['https://www.googleapis.com/auth/userinfo.email']
API_SERVICE_NAME = 'oauth2'
API_VERSION = 'v2'

app = flask.Flask(__name__)
# Note: A secret key is included in the sample so that it works.
# If you use this code in your application, replace this with a truly secret
# key. See https://flask.palletsprojects.com/quickstart/#sessions.
app.secret_key = 'REPLACE ME - this value is here as a placeholder.'


# Logged in sessions
search_history = {}

individual_search_history = []

# https://stackoverflow.com/questions/19473250/how-to-get-user-email-after-oauth-with-google-api-python-client
def get_user_info(credentials):
#  user_info_service = googleapiclient.discovery.build(
#      serviceName='oauth2', version='v2',
#      http=credentials.authorize(httplib2.Http()))
  user_info_service = googleapiclient.discovery.build(
      API_SERVICE_NAME, API_VERSION, credentials=credentials)
  user_info = None
  try:
    user_info = user_info_service.userinfo().get().execute()
  except errors.HttpError as e:
    logging.error('An error occurred: %s', e)
  if user_info and user_info.get('id'):
    return user_info
  else:
    raise NoUserIdException()


def get_session_id_from_cookie(request):
    sessionId = ''
    if 'Cookie' in request.headers:
        cookies = request.headers['Cookie']
        app.logger.info(cookies)
        if 'musicmarketplace-sessionId' in cookies:
            individual_cookies = cookies.split(";")
            for k in individual_cookies:
                parts = k.split("=")
                if parts[0].strip() == "musicmarketplace-sessionId":
                    sessionId = parts[1].strip()
                    break

    return sessionId


@app.route('/')
def index():
  return render_template("login.html")


@app.route('/test')
def test_api_request():
  if 'credentials' not in flask.session:
    return flask.redirect('login-with-google')

  # Load credentials from the session.
  credentials = google.oauth2.credentials.Credentials(
      **flask.session['credentials'])

  user_email = get_user_info(credentials)['email']
  print("User email:" + user_email)

  #drive = googleapiclient.discovery.build(
  #    API_SERVICE_NAME, API_VERSION, credentials=credentials)

  #files = drive.files().list().execute()

  # Save credentials back to session in case access token was refreshed.
  # ACTION ITEM: In a production app, you likely want to save these
  #              credentials in a persistent database instead.
  flask.session['credentials'] = credentials_to_dict(credentials)

  BASE_DIR = os.path.dirname(os.path.abspath(__file__))
  template_path = os.path.join(BASE_DIR, "templates/musicmarketplace.html")

  fp = open(template_path,"r")
  contents = fp.read()
  t = Template(contents)

  main_page = t.render(person=user_email)
  r = Response(main_page, status=200)

  return r


@app.route("/search_lessons_for", methods=['POST'])
def search_lessons_for():
    app.logger.info("Inside search_lessons_for")
    #instrument = request.args.get('instrument').strip().lower()
    instrument = request.form['lesson-name-input'].strip().lower()
    individual_search_history.append(instrument)

    BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    template_path = os.path.join(BASE_DIR, "templates/musicmarketplace.html")

    fp = open(template_path,"r")
    contents = fp.read()
    t = Template(contents)

    app.logger.info(individual_search_history)

    main_page = t.render(lesson_search_history=individual_search_history)

    return Response(main_page, status=200)


#@app.route('/authorize')
@app.route('/login-with-google',methods=['POST','GET'])
def authorize():
  # Create flow instance to manage the OAuth 2.0 Authorization Grant Flow steps.
  flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(
      CLIENT_SECRETS_FILE, scopes=SCOPES)

  # The URI created here must exactly match one of the authorized redirect URIs
  # for the OAuth 2.0 client, which you configured in the API Console. If this
  # value doesn't match an authorized URI, you will get a 'redirect_uri_mismatch'
  # error.
  flow.redirect_uri = flask.url_for('oauth2callback', _external=True)

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

  flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(
      CLIENT_SECRETS_FILE, scopes=SCOPES, state=state)
  flow.redirect_uri = flask.url_for('oauth2callback', _external=True)

  # Use the authorization server's response to fetch the OAuth 2.0 tokens.
  authorization_response = flask.request.url
  print("Authorization response:" + authorization_response)
  flow.fetch_token(authorization_response=authorization_response)

  # Store credentials in the session.
  # ACTION ITEM: In a production app, you likely want to save these
  #              credentials in a persistent database instead.
  credentials = flow.credentials
  flask.session['credentials'] = credentials_to_dict(credentials)

  return flask.redirect(flask.url_for('test_api_request'))


@app.route('/logout', methods=['POST'])
def revoke():
  if 'credentials' not in flask.session:
    return ('You need to <a href="/authorize">authorize</a> before ' +
            'testing the code to revoke credentials.')

  credentials = google.oauth2.credentials.Credentials(
    **flask.session['credentials'])

  revoke = requests.post('https://oauth2.googleapis.com/revoke',
      params={'token': credentials.token},
      headers = {'content-type': 'application/x-www-form-urlencoded'})

  status_code = getattr(revoke, 'status_code')
  if status_code == 200:
    print("Credentials successfully revoked.")

    # delete credentials from Flask session
    if 'credentials' in flask.session:
      del flask.session['credentials']

      # reset individual_search_history
      individual_search_history = []

    return render_template('login.html')
  else:
    return('An error occurred.') #+ print_index_table())


@app.route('/clear')
def clear_credentials():
  if 'credentials' in flask.session:
    del flask.session['credentials']
  return ('Credentials have been cleared.<br><br>' +
          print_index_table())


def credentials_to_dict(credentials):
  return {'token': credentials.token,
          'refresh_token': credentials.refresh_token,
          'token_uri': credentials.token_uri,
          'client_id': credentials.client_id,
          'client_secret': credentials.client_secret,
          'scopes': credentials.scopes}

def print_index_table():
  return ('<table>' +
          '<tr><td><a href="/test">Test an API request</a></td>' +
          '<td>Submit an API request and see a formatted JSON response. ' +
          '    Go through the authorization flow if there are no stored ' +
          '    credentials for the user.</td></tr>' +
          '<tr><td><a href="/authorize">Test the auth flow directly</a></td>' +
          '<td>Go directly to the authorization flow. If there are stored ' +
          '    credentials, you still might not be prompted to reauthorize ' +
          '    the application.</td></tr>' +
          '<tr><td><a href="/revoke">Revoke current credentials</a></td>' +
          '<td>Revoke the access token associated with the current user ' +
          '    session. After revoking credentials, if you go to the test ' +
          '    page, you should see an <code>invalid_grant</code> error.' +
          '</td></tr>' +
          '<tr><td><a href="/clear">Clear Flask session credentials</a></td>' +
          '<td>Clear the access token currently stored in the user session. ' +
          '    After clearing the token, if you <a href="/test">test the ' +
          '    API request</a> again, you should go back to the auth flow.' +
          '</td></tr></table>')


if __name__ == '__main__':
  # When running locally, disable OAuthlib's HTTPs verification.
  # ACTION ITEM for developers:
  #     When running in production *do not* leave this option enabled.
  os.environ['OAUTHLIB_INSECURE_TRANSPORT'] = '1'
  os.environ['OAUTHLIB_RELAX_TOKEN_SCOPE'] = '1'

  # Specify a hostname and port that are set as a valid redirect URI
  # for your API project in the Google API Console.
  app.run('localhost', 5003, debug=True)