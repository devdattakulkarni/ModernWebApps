import java.io.IOException;
import java.util.EnumSet;

import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;
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
                                response.getWriter().println("Domain:" + ck.getDomain());
                                response.getWriter().println("Path:" + ck.getPath());
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