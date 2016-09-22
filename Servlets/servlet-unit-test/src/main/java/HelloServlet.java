

import java.io.IOException;
import java.util.List;

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
	private JSoupService jsoupService;
	private CalculatorService calculatorService;
	
	@Override
	public void init() {
		try {
			super.init();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsoupService = new JSoupServiceImpl();	
	}
	
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
    
    protected int doGet4() throws Exception {
    	List<String> parsedURLs = jsoupService.getElements();
    	int count = parsedURLs.size();
    	return count;
    }
    
    protected String calculate(String [] values, String operator) throws Exception {
    	String result = calculatorService.calculate(values, operator);
    	return result;
    }    
    
    protected void setJsoupservice(JSoupService jsoupService) {
    	this.jsoupService = jsoupService;
    }
    
    protected void setCalculatorservice(CalculatorService calculatorService) {
    	this.calculatorService = calculatorService;
    }    
}
