Examples in this directory:

1) TestServerSocket.java

A bare bones HTTP server. It listens on port 8080.
The server responds to 'GET' requests from the clients. It adds 'Expires' header
to the data received from the client and sends it back.


2) TestHTTPClientSocket.java

A HTTP client that works with TestServerSocket.java