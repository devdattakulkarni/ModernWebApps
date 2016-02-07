#!/bin/bash

for i in {1..1000}
do
  curl -i http://localhost:8080/test-thread-servlet/threadServlet -H "X-User-Agent: TestRunner"
done