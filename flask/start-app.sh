#!/bin/bash

gunicorn --bind 0.0.0.0:5003 wsgi:app &
