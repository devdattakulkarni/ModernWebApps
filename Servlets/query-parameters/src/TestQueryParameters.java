
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TestQueryParameters extends HttpServlet {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        Map<String, String> userData;
        Map<String, String> loggedInUsers;
        
        public TestQueryParameters() {
                super();
        }
        
        @Override
		public void init(ServletConfig config) {
            userData = new HashMap<String, String>();
            loggedInUsers = new HashMap<String, String>();        	
        }
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) 
                        throws ServletException, IOException
        {
        	String value = request.getParameter("param1");
            response.getWriter().println("Param:param1" + " Value:" + value);
            
            System.out.println("");
            
            // Response is URLEncoded
            response.getWriter().println("Query String:" + request.getQueryString());
            
            // How to decode the URL encoded value?
            String queryString = URLDecoder.decode(request.getQueryString(), "UTF-8");            
            response.getWriter().println("Decoded Query String:" + queryString);
            
                
                // Regarding requests

                response.getWriter().println("Request URL:" + request.getRequestURL());               
                response.getWriter().println("Request URI:" + request.getRequestURI());
                response.getWriter().println("Servlet Path:" + request.getServletPath());
                            
            /*
                response.getWriter().println("<html>");
                response.getWriter().println("<form action=\"/qp/queryparam\" method=\"post\">");
                response.getWriter().println("<input type=\"text\" name=\"username\">");
                response.getWriter().println("<input type=\"text\" name=\"password\">");                
                response.getWriter().println("<input type=\"submit\">Log in </input>");
                response.getWriter().println("</form>");
                response.getWriter().println("</html>");
                */
                
                //response.getWriter().println("<html>");
                //response.getWriter().println("Hello again.");
                //response.getWriter().println("</html>");
                
                userData.put("V1", "V2");
        }
        

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
                {               
                        if (request.getParameter("username") != null &&
                                request.getParameter("password") != null) {
                                Cookie cookie = new Cookie("logged_in", "me");
                                cookie.setMaxAge(1000);
                                response.addCookie(cookie);
                                
                                response.getWriter().println("<html>");
                                response.getWriter().println("<br>Welcom, " + request.getParameter("username"));
                                response.getWriter().println("<form action=\"/qp/queryparam\" method=\"post\">");
                                response.getWriter().println("<input type=\"text\" name=\"type\">");
                                response.getWriter().println("<input type=\"text\" name=\"project\">");
                                response.getWriter().println("<input type=\"text\" name=\"year\">");
                                response.getWriter().println("<input type=\"submit\"></input>");
                                response.getWriter().println("</form>");
                                response.getWriter().println("</html>");
                                return;                         
                        }
                
                    String filename = getServletContext().getRealPath("/WEB-INF/userData.txt");
                    System.out.println("FileName:" + filename);

                    FileOutputStream f = new FileOutputStream(filename);
                    PrintWriter pw = new PrintWriter(f);
                        Cookie cookies[] = request.getCookies();                        
                        for(int i=0; i<cookies.length; i++) {
                                if (cookies[i].getName().equals("logged_in")) {
                                        Iterator it = userData.entrySet().iterator();
                                        while(it.hasNext()) {
                                                Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
                                                String x = entry.getKey() + ":" + entry.getValue();
                                                pw.println(x);
                                        }
                                }
                                pw.flush();
                                pw.close();
                                f.flush();
                                f.close();
                        }
                }
}