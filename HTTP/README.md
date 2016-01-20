Examples in this directory:

1) TestClientSocket.java:
This Java program uses Socket class to open a HTTP 1.1 connection
to a remote server whose name is passed on the command line.
The responses received from the server is written to file named "serveroutput.txt".
It is also printed to standard out.

Example usage:
javac TestClientSocket.java
java TestClientSocket www.cs.utexas.edu /


2) Base64EncodeDecode.java:
This Java program demonstrates how to encode a string in Base64 format.
The string to encode is currently hard coded in the program. Decoding functionality
is currently not implemented.


3) TestURLConnection.java:
This Java program uses URLConnection class to open a connection to remote HTTP
server whose URL is passed on the command line.


Running examples from Eclipse:

- Create a Java project
- In Package Explorer, right click on the project name, choose "New File"
- In the File explorer menu, select "Advanced" -> Link to file in the file system
- Browse to the directory where you have clone the repo and select one
  of the above files.
- Click Finish
- Select "Run Configurations"
- Under "Java Applications", select the file that you have just added to your project
- If your program takes command line arguments, you can pass them through the
  Arguments -> Program Arguments menu
