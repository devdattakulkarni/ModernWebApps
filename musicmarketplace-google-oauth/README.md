Steps
------

1. Create Virtual Environment
```python3 -m venv venv``` 

2. Activate Virtual Environment
```source venv/bin/activate```

3. Install dependencies
```pip3 install -r requirements.txt```

4. Create Google OAuth client
   - Login to https://console.cloud.google.com/apis/
   - Go to "Credentials->Create Credentials->OAuth client ID"
   - On "Create OAuth client ID" screen
     - Choose "Application type->Web application"
     - Give some name
     - In "Authorized redirect URIs", Add URI: http://localhost:5003/oauth2callback
     - Save
     - Download the client's ID and secret as JSON.
     - Save it in this folder by name "client_secret.json"

5. Run web app

   - When developing (Use Flask's built-in web server):
     ```python3 oauthexample.py```

6. Access web app

```curl http://localhost:5003```

7. Login to the Web app using your Google/Gmail credentials

8. If the login is successful, you should see the musicmarketplace html page with
   your gmail username in the top.

9. Try search

10. Try logout and login again.

--or--

Open http://localhost:5003 in browser

Enter any username/password and hit submit button (the web app does not support login functionality yet).


6. Debug web app

   - Put breakpoint in code:
     ```import pdb; pdb.set_trace()```

   - Check logs:
     ```more myapp.log```

7. Stop web app
 
   - If running directly using python interpreter
     ```Hit Cntrl+C```

   - If running using Gunicorn
      ```./stop-app.sh```


