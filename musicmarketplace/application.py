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

# List to hold registered lessons
lessons = []

# List to hold students who have signed up
signed_up_students = []


def render_main_page(persona, specific_message=''):
    BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    template_path = os.path.join(BASE_DIR, "templates/musicmarketplace.html")

    fp = open(template_path,"r")
    contents = fp.read()
    t = Template(contents)

    if persona == "learner":
        persona_specific_entities = signed_up_students
        persona_specific_my_lessons = "TODO"
        persona_specific_banner = 'TODO'
        search_display_style = 'display: block;'
        signups_style = 'display: none;'
        offer_lesson_or_search = "Search";
        offer_lesson_or_search_handler = "learn_music()";
        persona_specific_message = specific_message
    if persona == "teacher":
        persona_specific_entities = lessons
        persona_specific_my_lessons = "Registered teachers"
        persona_specific_banner = 'Teach Music'
        search_display_style = 'TODO'
        signups_style = 'display: block;'
        offer_lesson_or_search = "Offer Lesson";
        offer_lesson_or_search_handler = "take_input()";
        persona_specific_message = specific_message

    return t.render(name_lesson_list=persona_specific_entities,
                    my_lessons=persona_specific_my_lessons,
                    persona_specific_banner=persona_specific_banner,
                    search_display_style=search_display_style,
                    persona_specific_message=persona_specific_message,
                    signups_style=signups_style,
                    offer_lesson_or_search=offer_lesson_or_search,
                    offer_lesson_or_search_handler=offer_lesson_or_search_handler)


@app.route("/register_lesson", methods=['POST'])
def register_lesson():
    app.logger.info("Inside register_lesson")
    lesson_name = request.form['lesson-name-input']
    instrument = request.form['instrument-list-select']
    timings = request.form['timings-radio']
    days_of_the_week = request.form.getlist('day_of_the_week')
    demo_url = request.form['demo-url']
    # TODO: Parse instructor_name from the form
    instructor_name = "INSTRUCTOR_NAME"

    days_of_the_week_string = ','.join(days_of_the_week)

    app.logger.info("Lesson:%s Instrument:%s Timings:%s Days:%s Demo:%s",
                    lesson_name,instrument,timings,days_of_the_week,demo_url)

    lesson = {}
    lesson['instructor_name'] = instructor_name
    lesson['name'] = lesson_name
    lesson['lesson_name'] = lesson_name
    lesson['entity'] = instructor_name + " - " + lesson_name
    lesson['instrument'] = instrument
    lesson["demo_url"] = demo_url
    lesson["days"] = days_of_the_week_string
    lesson["timings"] = timings

    lessons.append(lesson)

    persona = "teacher"

    registered_message = "TODO"     # TODO
    return render_main_page(persona, specific_message=registered_message)


@app.route("/signups")
def get_signups():
    app.logger.info("Inside get_signups")
    ret_obj = {}
    ret_obj['signups'] = signed_up_students
    return ret_obj


# TODO: Complete search_lesson
@app.route("/searchlessons")
def search_lesson():
    app.logger.info("Inside search_lesson")

    ret_obj = {}
    ret_obj['lessons'] = "TODO: Return searched Lessons."
    return ret_obj


# TODO: Complete signup functionality
@app.route("/signup", methods=['POST'])
def signup():
    app.logger.info("Inside signup")

    learner_name = "TODO: Parse learner_name from signup form"
    lesson_name = "TODO: Parse lesson_name from signup form"

    app.logger.info("Learner name:%s Lesson name:%s", learner_name, lesson_name)

    student = {}
    student['name'] = learner_name
    student['lesson_name'] = lesson_name
    student['entity'] = learner_name + " - " + lesson_name
    signed_up_students.append(student)

    persona = "learner"

    registered_message = "TODO"     # TODO
    return render_main_page(persona, specific_message=registered_message)


@app.route("/login", methods=['POST'])
def login123():
    app.logger.info("Inside login")

    form = request.form
    app.logger.info(form)

    persona = request.form['persona-selector']

    if persona == 'learner':
        persona_specific_message = "TODO" # TODO - Add learner specific message
    if persona == 'teacher':
        persona_specific_message = 'Share your musical knowledge with others.'

    return render_main_page(persona, specific_message=persona_specific_message)


@app.route("/")
def index():
    app.logger.info("Inside index")
    return render_template('index.html')


if __name__ == "__main__":

    app.debug = True
    app.logger.info('Portal started...')
    app.run(host='0.0.0.0', port=5003) 
