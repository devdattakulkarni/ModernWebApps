import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;

/*
 * Author: Devdatta Kulkarni
 * 
 * User-Agent strings taken from:
 * http://www.useragentstring.com/pages/Mobile%20Browserlist/
 * 
 */

public class TestUserAgentHeader {
	
	public static void main(String []args) {
		
		Socket client = null;
		FileOutputStream f = null;

		try {

			if (args.length < 3) {
				System.out.println("Usage:");
				System.out.println("java TestUserAgentHeader <host> <resource> <ua1/ua2/generic>");
				System.exit(0);
			}			
			
			String host = args[0];
			String resource = args[1];
			String whichHeader = args[2];
			
			f = new FileOutputStream(whichHeader + "-test.html");			

			int portToUse = 80;
			
			client = new Socket(host, portToUse);
            client.setSoTimeout(1000);

			OutputStream out = client.getOutputStream();
			PrintWriter pout = new PrintWriter(out);
			pout.write("GET " + resource + " " + "HTTP/1.1\n");
			pout.write("Host: " + host + "\n");
			String ua1 = "Mozilla/5.0 (BlackBerry; U; BlackBerry 9900; en) AppleWebKit/534.11+ (KHTML, like Gecko) Version/7.1.0.346 Mobile Safari/534.11+";
			String ua2 = "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543 Safari/419.3";
			
			String ua = "";
			if (whichHeader.equalsIgnoreCase("ua1")) {
				ua = ua1;
				pout.write("User-Agent: " + ua + "\n");				
			} else if (whichHeader.equalsIgnoreCase("ua2")) {
				ua = ua2;
				pout.write("User-Agent: " + ua + "\n");				
			} 

			pout.write("\n");
			pout.flush();
			
			InputStream in = client.getInputStream();
			byte[] buffer = new byte[2000];
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