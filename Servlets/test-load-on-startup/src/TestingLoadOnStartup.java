import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class TestingLoadOnStartup extends HttpServlet {
        
        private static final long serialVersionUID = 1L;
        
        public TestingLoadOnStartup() {
        	System.out.println("Inside the constructor.");
        }
        
        public TestingLoadOnStartup(Map<String, String> map) {
        	
        }
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) 
                        throws ServletException, IOException
        {
                response.getWriter().println("TestLoadServlet.");        
        }
        

        @Override
        public void init(ServletConfig config) throws ServletException
        {
            //System.out.println("Servlet " + this.getServletName() + " has started.");
        	
        	System.out.println("Servlet " + config.getServletName() + " is being initialized.");
            
            printServletInitParams(config);
            
            printServletContextParams(config);                        
            
            for (int i=0; i<2; i++) {
                try {	
                	Thread.sleep(1000);
                } catch (InterruptedException e) {
                	e.printStackTrace();
                }
            }
        }

        @Override
        public void destroy()
        {
            System.out.println("Servlet " + this.getServletName() + " has stopped.");
        }
                
        private void printServletInitParams(ServletConfig config) {
            Enumeration<String> initParams = config.getInitParameterNames();
            System.out.println("Printing Servlet Init Params");
            while (initParams.hasMoreElements()) {
                String param = initParams.nextElement();
                String value = config.getInitParameter(param);
                System.out.println("Param:" + param + " Value:" + value);
            }        	
        }
        
        private void printServletContextParams(ServletConfig config) {
            ServletContext servletContext = config.getServletContext();
            Enumeration<String> contextParams = servletContext.getInitParameterNames();
            System.out.println("Printing Servlet Context Params");            
            while(contextParams.hasMoreElements()) {
            	String param = contextParams.nextElement();
            	String value = servletContext.getInitParameter(param);
            	System.out.println("Param:" + param + " Value:" + value);            	
            }            
        }        
}