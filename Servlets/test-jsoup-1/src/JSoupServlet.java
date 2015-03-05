import java.io.IOException;
import java.io.PrintWriter;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JSoupServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	JSoupHandler jsoupHandler;
	
	public JSoupServlet() {
		if (jsoupHandler == null) {
			jsoupHandler = new JSoupHandler();
		}
	}

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
                    throws ServletException, IOException
    {
		String project = request.getParameter("project");
		try {		    
			String source = "http://eavesdrop.openstack.org/irclogs/" + project;	
			Elements links = jsoupHandler.getElements(source);			
		    ListIterator<Element> iter = links.listIterator();
		    
		    // Code path:
		    while(iter.hasNext()) {
		    		Element e = (Element) iter.next();
		    		String s = e.html();
		    		String responseString = "";
		    		if ( s != null) {
		    			s = s.replace("#", "%23");
		    			responseString = source + s;
		    		}
		    		PrintWriter w = response.getWriter();
		    	    w.println(responseString);
		    }	    
		} catch (Exception exp) {
			exp.printStackTrace();
		}			
    }
	
	public void setJSoupHandler(JSoupHandler jsoupHandler) {
		this.jsoupHandler = jsoupHandler;
	}
}
