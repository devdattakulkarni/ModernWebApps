import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/*
 * Reference: http://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 */

public class TestServerSocket {
	
	public static void main(String [] args) throws Exception {
		
		ServerSocket serverSocket = new ServerSocket(8080);
		Socket clientSocket = serverSocket.accept();
		
		PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        
        String inputLine = null;
        String data = "";
        inputLine = in.readLine();
        while (!inputLine.equals("")) {
        	data += inputLine + "\n";
        	inputLine = in.readLine();
        }
        
        System.out.println("Receiving data done..");
        System.out.println("Sending response..");
        
    	if (data.contains("GET")) {
    		doGet(data, out);
    	}
		System.out.println("Server closing.");
    	clientSocket.close();
        serverSocket.close();
        System.out.println("Done.");
	}
	
	private static void doGet(String data, PrintWriter out) throws Exception {
        data += "Expires: " + getExpiresDate() + "\n";
        out.println(data);
	}
	
	private static String getExpiresDate() {
		Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    long currentTime = calendar.getTimeInMillis(); 
	    long tenMinutes = 600000;
	    long currentPlusTen = currentTime + tenMinutes;
	    calendar.setTimeInMillis(currentPlusTen);
	    
	    String expires = dateFormat.format(calendar.getTime());	    
	    return expires;
	}
}
