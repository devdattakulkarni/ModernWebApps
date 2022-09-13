from flask import Flask, render_template, redirect, request, url_for
from jinja2 import Environment, PackageLoader, select_autoescape

import requests

import os
import time
import random

from jinja2 import Template

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
            'filename': 'myapp.log',
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

app = Flask(__name__)

# Globals
lessons = []
lesson1 = {}
lesson1['name'] = "Guitar1"
lesson1['instrument'] = "Guitar"
lesson1["demo_url"] = "https://abc.com"
lesson1["days"] = "MW"
lesson1["timings"] = "3:30 - 4.00"

lesson2 = {}
lesson2['name'] = "Guitar2"
lesson2['instrument'] = "Guitar"
lesson2["demo_url"] = "https://def.com"
lesson2["days"] = "TTh"
lesson2["timings"] = "3:30 - 4.00"

lesson3 = {}
lesson3['name'] = "Piano1"
lesson3['instrument'] = "Piano"
lesson3["demo_url"] = "https://pia.no"
lesson3["days"] = "TTh"
lesson3["timings"] = "3:30 - 4.00"

lessons.append(lesson1)
lessons.append(lesson2)
lessons.append(lesson3)

signed_up_students = []


@app.route("/signups")
def get_signups():
    app.logger.info("Inside get_signups")

    names = ['alpha', 'beta', 'gamma', 'zeta', 'calisto', 'io', 'europa', 'ganymede']
    name = names[random.choice([1,2,3,4,5,6,7,8])-1]
    student = {}
    student['name'] = name
    signed_up_students.append(student)

    ret_obj = {}
    ret_obj['signups'] = signed_up_students
    return ret_obj


@app.route("/lessons")
def get_lesson():
    app.logger.info("Inside get_lesson")
    time.sleep(10)
    instrument = request.args.get('instrument').strip().lower()
    lessons_to_ret = []
    for lesson in lessons:
        if instrument in lesson['instrument'].lower():
            lessons_to_ret.append(lesson)
    ret_obj = {}
    ret_obj['lessons'] = lessons_to_ret
    return ret_obj


@app.route("/login", methods=['POST'])
def login123():
    app.logger.info("Inside login")
    BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    template_path = os.path.join(BASE_DIR, "templates/musicmarketplace.html")

    fp = open(template_path,"r")
    contents = fp.read()
    t = Template(contents)

    return t.render(lessons=lessons)


@app.route("/")
def index():
    app.logger.info("Inside index")
    return render_template('index.html')


if __name__ == "__main__":

    app.debug = True
    app.logger.info('Portal started...')
    app.run(host='0.0.0.0', port=5003) 
