Examples in this directory:

1) TestServerSocket.java

A bare bones HTTP server. It listens on port 8080.
The server responds to 'GET' requests from the clients. It adds 'Expires' header
to the data received from the client and sends it back.


2) TestHTTPClientSocket.java

A HTTP client that works with TestServerSocket.java


3) Servlet examples

Setup Eclipse and Tomcat:

+Eclipse Java EE IDE for Web Developers.
+Version: Luna Release (4.4.0)
+Build id: 20140612-0600

Tomcat:

+Download Apache Tomcat 8.0.9
+Setup Eclipse to use Tomcat
+Eclipse -> Preferences -> Server -> Runtime Environments -> Add
+Page 35 of ``Java for Web Applications’’ book


Create a 'Servlet Project':

In Eclipse, Servlet-based projects are called ‘Dynamic Web Project’
File -> New -> Other -> Web -> Dynamic Web Project
Give ‘Project Name’ -> ‘Finish’
Select “Target runtime”
Apache Tomcat v8.0
Dynamic Web Project structure
  /WebContent
         /META-INF
         /WEB-INF
Setting up the Dynamic Web Project
Add web.xml to WEB-INF
See web.xml in ‘hello1’ for example


Running Servlet Example:


Right click on the project
Select ‘Run As’ -> ‘Run on Server’
Choose the Tomcat v8.0 Server that we configured -> ‘Next’
Servlet will be now accessible at:
http://localhost:8080/<context_root>/<url-pattern>
Context Root
Right click on the project
Web Project settings
url-pattern
Specified in web.xml

