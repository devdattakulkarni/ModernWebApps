Steps
------

python3 -m venv venv 

source venv/bin/activate

pip3 install -r requirements.txt

./start-app.sh

curl http://localhost:5003

--or--

Open http://localhost:5003 in browser

Enter any username/password and hit submit button (the web app does not support login functionality yet).

more myapp.log

./stop-app.sh


