import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class TestServlet {

	public static void main(String [] args) throws InterruptedException {
		
		TestRunner testrunner1 = new TestRunner();
		Thread th1 = new Thread(testrunner1);
		th1.start();

		TestRunner testrunner2 = new TestRunner();
		Thread th2 = new Thread(testrunner2);
        th2.start();
		
		th1.join();
		th2.join();
	}
}

class TestRunner implements Runnable {

	@Override
	public void run() {
		String host = "localhost";
		String resource = "/test-thread-servlet/threadServlet";
		
		int portToUse = 8080;
		int numOfRequests = 5000;
		
		Socket client;
		try {
			for (int i=0; i<numOfRequests; i++) {
				client = new Socket(host, portToUse);
				OutputStream out = client.getOutputStream();
				PrintWriter pout = new PrintWriter(out);
				pout.write("GET " + resource + " " + "HTTP/1.1\n");
				pout.write("Host: " + host + "\n");
				pout.write("X-User-Agent: TestRunner\n");
				pout.write("\n");			
				pout.flush();
				out.close();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}