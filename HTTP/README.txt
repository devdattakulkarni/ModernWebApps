Examples in this directory:

1) TestClientSocket.java:
This Java program uses Socket class to open a HTTP 1.1 connection
to a remote server whose name is passed on the command line.
The responses received from the server is written to file named "serveroutput"
It is also printed to standard out.

Example usage:
javac TestClientSocket.java

- java TestClientSocket www.google.com / txt
  Output will be written to serveroutput.txt

- java TestClientSocket www.openstack.org /themes/openstack/images/customer-bestbuy.png png
  Output will be written to serveroutput.png


2) Base64EncodeDecode.java:
This Java program demonstrates how to encode a string in Base64 format.
The string to encode is currently hard coded in the program. Decoding functionality
is currently not implemented.


3) TestURLConnection.java:
This Java program uses URLConnection class to open a connection to remote HTTP
server whose URL is passed on the command line.


4) TestUserAgentHeader.java:
This Java program shows the effect of User-Agent request header on HTTP response.
The User-Agent request header is controlled from the command-line.
- If 'ua1' is passed as input for user agent, then the User-Agent header is set to 'BlackBerry',
simulating the request as being made from a BlackBerry.

- If 'ua2' is passed as input for user agent, then the User-Agent header is set to 'iPhone',
simulating the request as being made from a iPhone.

- If 'generic' is passed as input for user agent, the the User-Agent header is not set.

The program creates output files corresponding to the value passed in for the user agent input
parameter. The names of the files are ua1-test.html, ua2-test.html, generic-test.html.
The file contains the response headers from the response. To observe these files from a
web browser, open the file and delete all the headers from the top leaving the html content.
Observe the file in the browser. 

Example usage:
java TestUserAgentHeader www.google.com / ua1
java TestUserAgentHeader www.google.com / generic



5) RangeRequest.java:
This program demonstrates the effect of Range request header on HTTP response.
It takes in as input the range of bytes that you want to retrieve from a HTTP server for
the specified HTTP resource. It saves the reply in rangerequest.txt.
Note that the Range request header is respected by the HTTP server for a resource only
if that server replies with "Accept-Ranges: " response header.

Example usage:
java RangeRequest www.utexas.edu / 0-30

Above execution is requesting 31 bytes from http://www.utexas.edu/index.html





Running examples from Eclipse:

- Create a Java project
- In Package Explorer, right click on the project name, choose "New File"
- In the File explorer menu, select "Advanced" -> Link to file in the file system
- Browse to the directory where you have cloned the repo and select one
  of the above files.
- Click Finish
- Select "Run Configurations"
- Under "Java Applications", select the file that you have just added to your project
- If your program takes command line arguments, you can pass them through the
  Arguments -> Program Arguments menu
