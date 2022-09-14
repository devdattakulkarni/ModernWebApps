Steps
------

1. Create Virtual Environment
```python3 -m venv venv``` 

2. Activate Virtual Environment
```source venv/bin/activate```

3. Install dependencies
```pip3 install -r requirements.txt```

4. Run web app

   - When developing (Use Flask's built-in web server):
     ```python3 application.py```

   - In production (Use Gunicorn):
     ```./start-app.sh```

5. Access web app

Open ```http://localhost:5003``` in browser


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


