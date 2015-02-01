import java.net.*;
import java.io.IOException;
import java.io.InputStream;

/*
 * Author: Devdatta Kulkarni
 * 
 * Reference:
 * http://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html
 * 
 */

public class TestURLConnection {
	
	public static void main(String args[]) {
				
		if (args.length < 1) {
			System.out.println("Usage:");
			System.out.println("java TestUrlConnection <url>");
			System.exit(0);
		}
		URL url = null;
		try {
			url = new URL(args[0]);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		assert url != null;
		
		try {
			URLConnection connection = url.openConnection();
			System.out.println("Content-Encoding:" + connection.getContentEncoding());
			System.out.println("Content-Length:" + connection.getContentLength());
			System.out.println("Content-Type:" + connection.getContentType());
			
			System.out.println("Date:" + connection.getHeaderField("Date"));
			System.out.println("Transfer-Encoding:" + connection.getHeaderField("Transfer-Encoding"));
			System.out.println("Transfer-Length:" + connection.getHeaderField("Transfer-Length"));
			System.out.println("Expires:" + connection.getHeaderField("Expires"));
			System.out.println("Cache-Control:" + connection.getHeaderField("Cache-Control"));
			System.out.println("Connection:" + connection.getHeaderField("Connection"));
						
			System.out.println("******");
			
			InputStream input = connection.getInputStream();
			byte[] buffer = new byte[4096];
			int n = - 1;

			while ( (n = input.read(buffer)) != -1)
			{
			    if (n > 0)
			    {
			        System.out.write(buffer, 0, n);
			    }
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}