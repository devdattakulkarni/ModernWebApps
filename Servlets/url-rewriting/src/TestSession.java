import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
	
public class TestSession extends HttpServlet {
		
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException
		{
			HttpSession session = request.getSession(true);
			String url = request.getRequestURL().toString();
			if (session.isNew()) {
				String encodedURL = response.encodeURL(url);				
				writeResponse(response, "stranger", encodedURL);
			}			
			else {
				String encodedURL = response.encodeURL(url);				
				writeResponse(response, "friend", encodedURL);
			}
		}
		
		private void writeResponse(HttpServletResponse response, String you, String url) throws IOException {
			PrintWriter pw = response.getWriter();
			pw.println("<html>");
			pw.println("Hello, " + you);
			if (you.equalsIgnoreCase("stranger")) {
				pw.println("<br> Be a friend. Click <a href=" + url + "> here </a>");
			}
			pw.println("</html>");			
		}
}