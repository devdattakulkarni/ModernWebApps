import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;

/*
 * Author: Devdatta Kulkarni
 * 
 */

public class TestHttPClientSocket {
	
	public static void main(String []args) {
		
		Socket client = null;
		try {
			
			if (args.length < 2) {
				System.out.println("Usage:");
				System.out.println("java TestHttpClientSocket localhost /");
				System.exit(0);
			}
			
			String host = args[0];
			String resource = args[1];
			
			int portToUse = 8080;
			
			client = new Socket(host, portToUse);

			OutputStream out = client.getOutputStream();
			PrintWriter pout = new PrintWriter(out);

			System.out.println("Done sending data to the server.");			
			pout.write("GET " + resource + " " + "HTTP/1.1\n");
			pout.write("Host: " + host + "\n");
			pout.write("\n");
			pout.flush();

			/* Read the data back */			
			System.out.println("Reading data from server.");
			InputStream in = client.getInputStream();
			byte[] buffer = new byte[200];
			int n = - 1;
			
			FileOutputStream f = new FileOutputStream("serveroutput.txt");
			
			while ( (n = in.read(buffer)) != -1)
			{
			    if (n > 0)
			    {
			        System.out.write(buffer, 0, n);
			        f.write(buffer, 0, n);
			    }
			}
			f.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}		
	}
}