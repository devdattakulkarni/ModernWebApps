from flask import Flask, redirect, request, url_for
from flask import Response

import requests

import flask
from flask import request
from flask import Flask, render_template

from jinja2 import Template
import secrets

import base64
import json
import os

import time
import bcrypt


from flask import session

from multiprocessing import Process


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


app = Flask(__name__)

app.secret_key = secrets.token_hex() 


from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm import declarative_base, relationship
from sqlalchemy import Column, Integer, ForeignKey, String

from logging.config import dictConfig


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
            'filename': 'weatherportal.log',
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


# SQLite Database creation
Base = declarative_base()
engine = create_engine("sqlite:///weatherportal.db", echo=True, future=True,connect_args={'check_same_thread': False})
DBSession = sessionmaker(bind=engine)


# Cities names
in_mem_cities = []

# dict; {username: [{name:Austin, year:2023, month:08, params:[TMAX,TMIN]},{}]}
in_mem_user_cities = {}




class CityDetails:
    def __init__(self, name='', month='', year='', params=[]):
        self.name = name
        self.month = month
        self.year = year
        self.params = params
   

class Admin(Base):
    __tablename__ = 'admin'
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String)
    password = Column(String)
    registeredcities = relationship("City", cascade="all, delete-orphan")

    def __repr__(self):
        return "<Admin(name='%s')>" % (self.name)

    # Ref: https://stackoverflow.com/questions/5022066/how-to-serialize-sqlalchemy-result-to-json
    # 1. Assignment 5: Modify the method to not return password in the returned fields
    def as_dict(self):
        fields = {}
        for c in self.__table__.columns:
            fields[c.name] = getattr(self, c.name)
        return fields


class User(Base):
    __tablename__ = 'user'
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String)
    password = Column(String)

    def __repr__(self):
        return "<User(name='%s')>" % (self.name)

    # Ref: https://stackoverflow.com/questions/5022066/how-to-serialize-sqlalchemy-result-to-json
    # 2. Assignment 5: Modify the method to not return password in the returned fields
    def as_dict(self):
        fields = {}
        for c in self.__table__.columns:
            fields[c.name] = getattr(self, c.name)
        return fields


class City(Base):
    __tablename__ = 'city'
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String)
    url = Column(String)
    adminId = Column(ForeignKey("admin.id"))
    usercities = relationship("UserCity", cascade="all, delete-orphan")

    def __repr__(self):
        return "<City(name='%s')>" % (self.name)

    # Ref: https://stackoverflow.com/questions/5022066/how-to-serialize-sqlalchemy-result-to-json
    def as_dict(self):
        fields = {}
        for c in self.__table__.columns:
            fields[c.name] = getattr(self, c.name)
        return fields


class UserCity(Base):
    __tablename__ = 'usercity'
    id = Column(Integer, primary_key=True, autoincrement=True)
    cityId = Column(ForeignKey("city.id"))
    userId = Column(ForeignKey("user.id"))
    year = Column(String)
    month = Column(String)
    weather_params = Column(String)

    def __repr__(self):
        return "<UserCity(cityId='%d' userId='%d')>" % (self.cityId, self.userId)

    # Ref: https://stackoverflow.com/questions/5022066/how-to-serialize-sqlalchemy-result-to-json
    def as_dict(self):
        fields = {}
        for c in self.__table__.columns:
            fields[c.name] = getattr(self, c.name)
        return fields


class WeatherParameter(Base):
    __tablename__ = 'weatherparameter'
    id = Column(Integer, primary_key=True, autoincrement=True)
    year_month_param = Column(String)
    values = Column(String)
    cityId = Column(ForeignKey("city.id"))

    def __repr__(self):
        return "<WeatherParameter(name='%s')>" % (self.name)

    # Ref: https://stackoverflow.com/questions/5022066/how-to-serialize-sqlalchemy-result-to-json
    def as_dict(self):
        fields = {}
        for c in self.__table__.columns:
            fields[c.name] = getattr(self, c.name)
        return fields


weather_parameters_to_track = ['TMAX','TMIN','PRCP','SNOW']

Base.metadata.create_all(engine)


class ETL():

    def __init__(self):
        pass

    def _get_cities(self):
        dbsession = DBSession()
        cities = dbsession.query(City)
        return cities

    def _load_data(self):
        cities = self._get_cities()
        for city in cities:
            city_name = city.name
            city_url = city.url
            r = requests.get(city_url)
            # 1. Assignment 4 TODO: Check if the city has data available by checking for return code 404
            city_data = r.text
            data_dir = os.getcwd() + "/data"
            os.system("mkdir -p " + data_dir)
            fp = open(data_dir + "/" + city_name,"w")
            fp.write(city_data)

        dbsession = DBSession()
        for filename in os.listdir(os.getcwd() + "/data"):
            print("-------")
            print(filename)
            fPath = os.getcwd() + "/data/" + filename
            fp = open(fPath,"r")
            lines = fp.readlines()
            param_values = {}
            #for l in range(0, len(lines)-2, 2):
            for l in range(0, len(lines)):
                if l+1 > len(lines):
                    break
                #line = lines[l] + lines[l+1]
                line = lines[l]
                #print(line)
                parts = line.split(" ")
                year_month_param = parts[0]
                rest = year_month_param[11:]
                year = rest[0:4]
                month = rest[4:6]
                param = rest[6:]
                if param in weather_parameters_to_track:
                    #print(year + " " + month + " " + param)
                    values1 = []
                    values = []
                    for i in range(len(parts)):
                        if i > 0:
                            part = parts[i].strip()
                            if i%2 == 0:
                                if part and (part != '0T' and part != '0P'):
                                    values1.append(part)
                    for j in range(0, len(values1), 2):
                        values.append(values1[j])
                    param_values[year+"-"+month+"-"+param] = values
            #print(param_values)
            for key, val in param_values.items():
                valString = ','.join(val)
                city = dbsession.query(City).filter_by(name=filename).first()
                if city != None:
                    exists = dbsession.query(WeatherParameter).filter_by(cityId=city.id, year_month_param=key)
                    if exists.count() == 0:
                        weather_params = WeatherParameter(year_month_param=key, values=valString, cityId=city.id)
                        dbsession.add(weather_params)
                        dbsession.commit()

    def run(self):
        while True:
            time.sleep(10)
            app.logger.info("Inside ETL.")
            self._load_data()

etl = ETL()

# Start the ETL process
p = Process(target=etl.run)
p.start()


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
        raise NoUserIdException()


def credentials_to_dict(credentials):
  return {'token': credentials.token,
          'refresh_token': credentials.refresh_token,
          'token_uri': credentials.token_uri,
          'client_id': credentials.client_id,
          'client_secret': credentials.client_secret,
          'scopes': credentials.scopes}



## Admin REST API
@app.route("/admin", methods=['POST'])
def add_admin():
    app.logger.info("Inside add_admin")
    data = request.json
    app.logger.info("Received request:%s", str(data))

    name = data['name']
    password = data['password']


    session = DBSession()
    admin = session.query(Admin).filter_by(name=name).first()

    if admin != None:
        status = ("Admin with name {name} already exists.\n").format(name=name)
        return Response(status, status=400)
    else:
        # 3. Assignment 5: Use bcrypt to encrypt the password.
        admin = Admin(name=name, password=password)
        session.add(admin)
        session.commit()

    return admin.as_dict()


@app.route("/admin")
def get_admins():
    app.logger.info("Inside get_admins")
    ret_obj = {}

    session = DBSession()
    admins = session.query(Admin)
    admin_list = []
    for admin in admins:
        admin_list.append(admin.as_dict())

    ret_obj['admins'] = admin_list
    return ret_obj


@app.route("/admin/<id>")
def get_admin_by_id(id):
    app.logger.info("Inside get_admin_by_id %s\n", id)

    session = DBSession()
    admin = session.get(Admin, id)

    app.logger.info("Found admin:%s\n", str(admin))
    if admin == None:
        status = ("Admin with id {id} not found\n").format(id=id)
        return Response(status, status=404)
    else:
        return admin.as_dict()


@app.route("/admin/<id>", methods=['DELETE'])
def delete_admin_by_id(id):
    app.logger.info("Inside delete_admin_by_id %s\n", id)

    session = DBSession()
    admin = session.query(Admin).filter_by(id=id).first()

    app.logger.info("Found admin:%s\n", str(admin))
    if admin == None:
        status = ("Admin with id {id} not found.\n").format(id=id)
        return Response(status, status=404)
    else:
        session.delete(admin)
        session.commit()
        status = ("Admin with id {id} deleted.\n").format(id=id)
        return Response(status, status=200)


## User REST API
@app.route("/users", methods=['POST'])
def add_user():
    app.logger.info("Inside add_user")
    data = request.json
    app.logger.info("Received request:%s", str(data))

    name = data['name']
    password = data['password']

    # 4. Assignment 5: Use bcrypt to encrypt the password.
    newuser = User(name=name, password=password)

    session = DBSession()
    user = session.query(User).filter_by(name=name).first()
    if user == None:
        session.add(newuser)
        session.commit()

        return newuser.as_dict()
    else:
        status = ("User with name {name} already exists.\n").format(name=name)
        return Response(status, status=400)

@app.route("/users")
def get_users():
    app.logger.info("Inside get_users")
    ret_obj = {}

    session = DBSession()
    users = session.query(User)
    user_list = []
    for user in users:
        user_list.append(user.as_dict())

    ret_obj['users'] = user_list
    return ret_obj

@app.route("/users/<id>")
def get_user_by_id(id):
    app.logger.info("Inside get_user_by_id %s\n", id)

    session = DBSession()
    user = session.get(User, id)

    app.logger.info("Found user:%s\n", str(user))
    if user == None:
        status = ("User with id {id} not found\n").format(id=id)
        return Response(status, status=404)
    else:
        return user.as_dict()


@app.route("/users/<id>", methods=['DELETE'])
def delete_user_by_id(id):
    app.logger.info("Inside delete_user_by_id %s\n", id)

    session = DBSession()
    user = session.query(User).filter_by(id=id).first()

    app.logger.info("Found user:%s\n", str(user))
    if user == None:
        status = ("User with id {id} not found.\n").format(id=id)
        return Response(status, status=404)
    else:
        session.delete(user)
        session.commit()
        status = ("User with id {id} deleted.\n").format(id=id)
        return Response(status, status=200)


## City REST API
@app.route("/admin/<admin_id>/cities", methods=['POST'])
def add_city(admin_id):
    app.logger.info("Inside add_city")
    data = request.json
    app.logger.info("Received request:%s", str(data))

    name = data['name']
    url = data['url']

    session = DBSession()
    admin = session.query(Admin).filter_by(id=admin_id).first()

    if admin == None:
        status = ("Admin with id {id} not found.\n").format(id=admin_id)
        return Response(status, status=404)
    else:
        city = City(name=name, url=url, adminId=admin.id)

        # 2. Assignment 4: Check if City with name already exists; Don't add if it already exists
        existing_city = session.query(City).filter_by(name=name).first()
        if existing_city != None:
            status = ("City with name {name} already exists.\n").format(name=name)
            return Response(status, status=404)
        else:
            session.add(city)
            session.commit()

    return city.as_dict()


@app.route("/admin/<admin_id>/cities", methods=['GET'])
def get_cities(admin_id):
    app.logger.info("Inside get_cities")

    session = DBSession()
    admin = session.query(Admin).filter_by(id=admin_id).first()

    if admin == None:
        status = ("Admin with id {id} not found.\n").format(id=admin_id)
        return Response(status, status=404)
    else:
        cities = session.query(City).filter_by(adminId=admin.id)
        city_list = []
        ret_obj = {}
        for city in cities:
            city_list.append(city.as_dict())
        ret_obj['cities'] = city_list
        return ret_obj


@app.route("/admin/<admin_id>/cities/<city_id>", methods=['GET'])
def get_city(admin_id, city_id):
    app.logger.info("Inside get_city")

    session = DBSession()
    admin = session.query(Admin).filter_by(id=admin_id).first()

    if admin == None:
        status = ("Admin with id {id} not found.\n").format(id=admin_id)
        return Response(status, status=404)
    else:
        city = session.query(City).filter_by(adminId=admin.id,id=city_id).first()
        if city == None:
            status = ("City with id {id} not found.\n ").format(id=city_id)
            return Response(status, status=404)
        return city.as_dict()


## User City REST API
@app.route("/users/<user_id>/cities", methods=['POST'])
def add_user_city(user_id):
    app.logger.info("Inside add_user_city")
    data = request.json
    app.logger.info("Received request:%s", str(data))

    name = data['name']
    month = data['month']
    year = data['year']
    weather_params = data['weather_params']

    session = DBSession()
    user = session.query(User).filter_by(id=user_id).first()

    if user == None:
        status = ("User with id {id} not found.\n").format(id=user_id)
        return Response(status, status=404)

    city = session.query(City).filter_by(name=name).first()
    if city == None:
        status = ("City with name {name} not found.\n").format(name=name)
        return Response(status, status=404)

    usercity = UserCity(weather_params=weather_params, month=month, year=year, userId=user.id, cityId=city.id)
    session.add(usercity)
    session.commit()

    return usercity.as_dict()

@app.route("/users/<user_id>/cities", methods=['GET'])
def get_city_by_name(user_id):
    op = {}
    app.logger.info("Inside get_city_by_name")
    city_name = request.args.get('name').strip()
    app.logger.info("City name:" + city_name)

    session = DBSession()
    user = session.query(User).filter_by(id=user_id).first()

    if user == None:
        status = ("User with id {id} not found.\n").format(id=user_id)
        return Response(status, status=404)

    city = session.query(City).filter_by(name=city_name).first()
    if city == None:
        status = ("City with name {name} not found.\n").format(name=city_name)
        return Response(status, status=404)

    usercity = session.query(UserCity).filter_by(cityId=city.id,userId=user_id).first()
    if usercity == None:
        status = ("City with name {city_name} not being tracked by the user {user_name} .\n").format(city_name=city_name,
                user_name=user.name)
        return Response(status, status=404)

    resp_json = {}
    resp_json["month"] = usercity.month
    resp_json["year"] = usercity.year
    resp_json["name"] = city.name
    resp_json["weather_params"] = usercity.weather_params
    return resp_json


def get_user_cities(dbsession, userid):
    cities = []
    usercities = dbsession.query(UserCity).filter_by(userId=userid)
    for usr in usercities:
        cty_list = dbsession.query(City).filter_by(id=usr.cityId)
        if cty_list.count() == 1:
            cty = cty_list.first()
            city = {}
            city['name'] = cty.name
            cities.append(city)
    return cities


def get_admin_cities(dbsession):
    cities = []
    admin_cities = dbsession.query(City)
    for cty in admin_cities:
        city = {}
        city['name'] = cty.name
        cities.append(city)
    return cities 


@app.route("/status", methods=['GET'])
def city_status():
    op = {}
    app.logger.info("Inside city_status")
    city_name = request.args.get('city').strip()
    app.logger.info("City status:" + city_name)
    
    username = ''
    if 'username' in session:
        username = session['username']

    app.logger.info("Username:" + username + " City:" + city_name)
    cities_to_check = []
    if username in in_mem_user_cities:
        cities_to_check = in_mem_user_cities[username]
    else:
        cities_to_check = in_mem_cities

    city_details = None
    for c in cities_to_check:
        if c.name == city_name:
            city_details = c
            break

    op['name'] = city_details.name
    op['month'] = city_details.month
    op['year'] = city_details.year
    op['params'] = city_details.params
    app.logger.info(op)
    return json.dumps(op)


## 3. Assignment 4 
## Expected Output:
### {'2023-08-TMAX': '411,406,400,406,411,406,411,411,422,417,422,411,411,411,389,400,433,411,400,417,-9999,-9999,-9999', '2023-08-TMIN': '256,261,256,261,256,256,256,256,250,256,261,267,261,250,267,228,250,250,239,256,-9999,-9999,-9999', '2023-08-PRCP': '-9999,-9999,-9999'}
@app.route("/weather_params", methods=['GET'])
def city_status_graph():
    op = {}
    app.logger.info("Inside city_status")
    city_name = request.args.get('city').strip()
    app.logger.info("City status:" + city_name)
    
    username = ''
    if 'username' in session:
        username = session['username']

    app.logger.info("Username:" + username + " City:" + city_name)    

    dbsession = DBSession()
    users = dbsession.query(User).filter_by(name=username)
    user = users.first()

    cities = dbsession.query(City).filter_by(name=city_name)
    city = cities.first()

    usercities = dbsession.query(UserCity).filter_by(cityId=city.id,userId=user.id)
    usercity = usercities.first()
    month = usercity.month
    year = usercity.year
    weather_params = usercity.weather_params
    individual_params = weather_params.split(',')
    for param in individual_params:
        param = param.strip()
        year_month_param = year + "-" + month + "-" + param
        param_values = dbsession.query(WeatherParameter).filter_by(cityId=city.id, year_month_param=year_month_param)
        if param_values.count() > 0:
            param_value_str = param_values.first().values
            op[year_month_param] = param_value_str
        else:
            op[year_month_param] = "" 

    app.logger.info(op)
    return json.dumps(op)


## Not needed
@app.route("/addcity", methods=['POST','GET'])
def addcity():
    app.logger.info("Inside addcity")
    city_name = request.args.get('city_name').strip()
    city = CityDetails(name=city_name)
    in_mem_cities.append(city)

    username = ''
    if 'username' in session:
        username = session['username']

    user_cities = in_mem_cities
    return render_template('welcome.html',
            welcome_message = "Personal Weather Portal - Admin Panel",
            status_string="Registered city " + city_name + ".",
            cities=user_cities,
            available_cities=admin_cities,
            name=username,
            addButton_style="display:inline;",
            addCityForm_style="display:none;",
            regButton_style="display:none;",
            regForm_style="display:none;",
            status_style="display:block;")


@app.route("/registercity", methods=['POST','GET'])
def registercity():
    app.logger.info("Inside registercity")
    city_name = request.args.get('city_name').strip()
    year = request.args.get('year').strip()
    month = request.args.get('month').strip()

    user_weather_params = [] #weather_parameters_to_track = ['TMAX','TMIN','PRCP','SNOW']
    if 'max_temp' in request.args:
        user_weather_params.append('TMAX')
    if 'min_temp' in request.args:
        user_weather_params.append('TMIN')
    if 'precipitation' in request.args:
        user_weather_params.append('PRCP')
    if 'snow' in request.args:
        user_weather_params.append('SNOW')

    app.logger.info("City:" + city_name + " Year:" + year + " Month:" + month)
    app.logger.info(user_weather_params)

    username = ''
    if 'username' in session:
        username = session['username']

    dbsession = DBSession()
    users = dbsession.query(User).filter_by(name=username)
    cities = None
    try:
        cities = dbsession.query(City).filter_by(name=city_name)
    except Exception as e:
        app.logger.info(e)
    
    user = users.first()
    user_cities = get_user_cities(dbsession, user.id)

    my_cities = []
    if username in in_mem_user_cities:
        my_cities = in_mem_user_cities[username]
    for c in in_mem_cities:
        if c.name == city_name:
            city_details = CityDetails(name=city_name,year=year, month=month, params=user_weather_params)
            my_cities.append(city_details)
    in_mem_user_cities[username] = my_cities

    present = False
    if cities.count() > 0: 
        for c in cities:
            if c.name == city_name:
                present = True
                break

    # 4. Assignment 4 - Return admin_cities
    admin_cities = get_admin_cities(dbsession)

    if present:
        city = cities.first()
        presentcity = dbsession.query(UserCity).filter_by(userId=user.id, cityId=city.id)
        if presentcity.count() == 0:
            usercity = UserCity(userId=user.id, cityId=city.id, year=year, month=month, 
                                weather_params=','.join(user_weather_params))
            dbsession.add(usercity)
            dbsession.commit()

        # Get the new city
        user_cities = get_user_cities(dbsession, user.id)
        return render_template('welcome.html',
                welcome_message = "Personal Weather Portal",
                cities=user_cities,
                available_cities=admin_cities,
                name=username,
                status_string="Registered city " + city_name + ".",
                addButton_style="display:none;",
                addCityForm_style="display:none;",                
                regForm_style="display:none;",
                status_style="display:block;")
    else:
        return render_template('welcome.html',
                cities=user_cities,
                available_cities=admin_cities,
                welcome_message = "Personal Weather Portal",
                name=username,
                status_string="Weather data for city " + city_name + " not available.",
                addButton_style="display:none;",
                addCityForm_style="display:none;",
                regForm_style="display:none;",
                status_style="display:block;")


# 5. Assignment 5: 
# - Connect the "login-using-google" form with this method
# - For http methods list in the definition, use POST and GET
def authorize():
  if not os.path.exists(CLIENT_SECRETS_FILE):
      return render_template('google-oauth-client-secrets-file-missing.html')

  # Create flow instance to manage the OAuth 2.0 Authorization Grant Flow steps.
  flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(
      CLIENT_SECRETS_FILE, scopes=SCOPES)

  # The URI created here must exactly match one of the authorized redirect URIs
  # for the OAuth 2.0 client, which you configured in the API Console. If this
  # value doesn't match an authorized URI, you will get a 'redirect_uri_mismatch'
  # error.
  redirect_uri = flask.url_for('oauth2callback', _external=True)
  parts = redirect_uri.split("/oauth2callback")
  print(parts)
  if ":5009" not in redirect_uri:
    flow.redirect_uri = parts[0] + ":5009/oauth2callback"
  else:
    flow.redirect_uri = redirect_uri
  print(flow.redirect_uri)

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
  # In a production app, you likely want to save these
  # credentials in a persistent database instead.
  credentials = flow.credentials
  flask.session['credentials'] = credentials_to_dict(credentials)

  return flask.redirect(flask.url_for('login'))



@app.route("/logout",methods=['GET'])
def logout():
    app.logger.info("Logout called.")
    session.pop('username', None)
    app.logger.info("Before returning...")

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

    return render_template('index.html')


@app.route("/login", methods=['POST','GET'])
def login():
    username = ''
    password = ''
    if 'username' in request.form:
        username = request.form['username'].strip()
        password = request.form['password'].strip()
    app.logger.info("Username:%s", username)
    app.logger.info("Password:%s", password)

    # 5. Assignment 4 - Check if user is present
    if username != '':
        dbsession = DBSession()
        users = dbsession.query(User).filter_by(name=username)
        if users.count() == 0:
            return render_template('not-found.html',user=username)
        else:
            # 6. Assignment 5:
            # - Check that <username, password> exists in the database
            # - Note that password will be encrypted in the DB.
            # - You will have to use bcrypt's checkpw method to check the password.
            app.logger.info("TODO: Verify the user and password.")

    # Load credentials from the session.
    if 'credentials' in flask.session:
        credentials = google.oauth2.credentials.Credentials(
              **flask.session['credentials'])

        user_email = get_user_info(credentials)['email']
        print("User email:" + user_email)
        username = user_email

        # Assignment 5: 
        # - Insert User in the DB
        # - Leave password empty

        flask.session['credentials'] = credentials_to_dict(credentials)

    session['username'] = username

    dbsession = DBSession()
    users = dbsession.query(User).filter_by(name=username)

    # 6. Assignment 4 - Return admin_cities
    admin_cities = get_admin_cities(dbsession)

    runs = []
    user_cities = []
    if users.count() > 0:
        user = users.first()
        user_cities = get_user_cities(dbsession, user.id)
    else:
        user = User(name=username, password=password)
        dbsession = DBSession()
        dbsession.add(user)
        dbsession.commit()

    my_cities = []
    if username in in_mem_user_cities:
        my_cities = in_mem_user_cities[username]
    return render_template('welcome.html',
            welcome_message = "Personal Weather Portal",
            cities=user_cities,
            available_cities=admin_cities,
            name=username,
            addButton_style="display:none;",
            addCityForm_style="display:none;",
            regButton_style="display:inline;",
            regForm_style="display:inline;",
            status_style="display:none;")


@app.route("/")
def index():
    return render_template('index.html')



@app.route("/adminlogin", methods=['POST'])
def adminlogin():
    username = request.form['username'].strip()
    password = request.form['password'].strip()
    app.logger.info("Username:%s", username)
    app.logger.info("Password:%s", password)

    session['username'] = username

    user_cities = in_mem_cities
    return render_template('welcome.html',
            welcome_message = "Personal Weather Portal - Admin Panel",
            cities=user_cities,
            name=username,
            addButton_style="display:inline;",
            addCityForm_style="display:inline;",
            regButton_style="display:none;",
            regForm_style="display:none;",
            status_style="display:none;")


@app.route("/adminindex")
def adminindex():
    return render_template('adminindex.html')

if __name__ == "__main__":

    app.debug = False
    app.logger.info('Portal started...')
    app.run(host='0.0.0.0', port=5009, ssl_context='adhoc')
