import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class TestingLoadOnStartup extends HttpServlet {
        
        private static final long serialVersionUID = 1L;
        ServletContext sContext;
                        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) 
                        throws ServletException, IOException
        {
                response.getWriter().println("TestLoadServlet.");
                printServletContextParams(sContext);
        }        

        @Override
        public void init(ServletConfig config) throws ServletException
        {        	
        		System.out.println("Servlet " + config.getServletName() + " is being initialized.");
        		
        		sContext = config.getServletContext();
            printServletConfigParams(config);            
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
                
        private void printServletConfigParams(ServletConfig config) {
            Enumeration<String> initParams = config.getInitParameterNames();
            System.out.println("*** Servlet Init Params ***");
            while (initParams.hasMoreElements()) {
                String param = initParams.nextElement();
                String value = config.getInitParameter(param);
                System.out.println("Param:" + param + " Value:" + value);
            }
            System.out.println("*** Servlet Init Params ***");            
        }
        
        private void printServletContextParams(ServletConfig config) {
            ServletContext servletContext = config.getServletContext();
            printServletContextParams(servletContext);
        }        
        
        private void printServletContextParams(ServletContext servletContext) {
            Enumeration<String> contextParams = servletContext.getInitParameterNames();
            System.out.println("*** Servlet Context Params ***");            
            while(contextParams.hasMoreElements()) {
            	String param = contextParams.nextElement();
            	String value = servletContext.getInitParameter(param);
            	System.out.println("Param:" + param + " Value:" + value);            	
            }
            System.out.println("*** Servlet Context Params ***");            
        }
}