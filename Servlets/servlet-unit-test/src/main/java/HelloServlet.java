

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
                    throws ServletException, IOException
    {
		response.getWriter().println("Hello world.");
    }
    
    protected void doGet2(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException
    {
    		String helloTarget = "world.";
    		if (request.getParameter("username") != null) {
    			helloTarget = request.getParameter("username");
    		}
    		response.getWriter().println("Hello " + helloTarget);
    }    
    
    protected void doGet3(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException
    {
    		if (request.getParameter("username") != null) {
    			String username = request.getParameter("username");
    			Cookie newUser = new Cookie(username, username);
    			response.addCookie(newUser);
    		}
    }        

}
