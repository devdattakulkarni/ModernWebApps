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

public class RangeRequest {
	
	public static void main(String []args) {
		
		Socket client = null;
		FileOutputStream f = null;

		try {
			f = new FileOutputStream("rangerequest.txt");		     

			if (args.length < 3) {
				System.out.println("Usage:");
				System.out.println("java RangeRequest <host> <resource> <byte-range>");
				System.out.println("java RangeRequest www.cs.utexas.edu /~devdatta/index.html 0-300");				
				System.exit(0);
			}
			
			String host = args[0];
			String resource = args[1];
			String byteRange = args[2];

			int portToUse = 80;
			
			client = new Socket(host, portToUse);
            client.setSoTimeout(1000);

			OutputStream out = client.getOutputStream();
			PrintWriter pout = new PrintWriter(out);
			pout.write("GET " + resource + " " + "HTTP/1.1\n");
			pout.write("Host: " + host + "\n");
			pout.write("Range: bytes=" + byteRange + "\n");
			pout.write("\n");
			pout.flush();
			
			InputStream in = client.getInputStream();
			byte[] buffer = new byte[200];
			int n = - 1;

			while ( (n = in.read(buffer)) != -1)
			{
			    if (n > 0)
			    {
			        System.out.write(buffer, 0, n);
			        f.write(buffer, 0, n);
			    }
			}
			if (f != null) {
			    f.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			if (f != null) {
				try {
					f.close();
			} catch (Exception e1) {
			    	e1.printStackTrace();
				}
		    }
		}
	}
}