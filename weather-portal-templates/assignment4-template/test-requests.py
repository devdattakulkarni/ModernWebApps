import requests

r = requests.get('https://www.cs.utexas.edu/~devdatta/Houston.txt')

print(r.text)
