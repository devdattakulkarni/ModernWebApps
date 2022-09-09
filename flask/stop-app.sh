#!/bin/bash

ps -eaf | grep "gunicorn --bind 0.0.0.0:5003" | grep -v grep | awk '{print $2}' | xargs kill -9
