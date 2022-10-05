#!/bin/bash

# Use a test database
export MUSIC_MARKETPLACE_DB='test.db'

# Start the REST API in background
python3 application.py &
pid=$!
#echo $pid

# Wait for the REST API to become ready
sleep 5

# Exercise the REST API programmatically
python3 test.py

# Shutdown the REST API process and all its children
pkill -9 -P $pid

# Delete the db file
rm test.db