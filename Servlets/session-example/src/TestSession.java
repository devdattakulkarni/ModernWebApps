import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestSession extends HttpServlet {
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) 
                        throws ServletException, IOException
        {               
                Cookie cookies [] = request.getCookies();
                boolean friend = false;
                for(int i=0; cookies != null && i<cookies.length; i++) {
                        Cookie ck = cookies[i];
                        String cookieName = ck.getName();
                        String cookieValue = ck.getValue();
                        if ((cookieName != null && cookieName.equals("not-a-stranger-anymore")) 
                                        && cookieValue != null && cookieValue.equals("friend")) {
                                response.getWriter().println("Hello, friend.");
                                friend = true;
                        }
                }
                if (!friend) {
                        Cookie cookie = new Cookie("not-a-stranger-anymore","friend");
                        cookie.setDomain("localhost");
                        cookie.setPath("/session-example" + request.getServletPath());
                        cookie.setMaxAge(1000);
                        response.addCookie(cookie);
                        response.getWriter().println("Hello, stranger.");
                }
        }
}