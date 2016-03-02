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
		PrintWriter w = response.getWriter();
		String project = request.getParameter("project");
		try {		    
			String source = "http://eavesdrop.openstack.org/irclogs/%23" + project;	
			Elements links = jsoupHandler.getElements(source);

		    if (links != null) {
			    ListIterator<Element> iter = links.listIterator();		    	
			    while(iter.hasNext()) {
		    			Element e = (Element) iter.next();
		    			String s = e.html();
		    			if ( s != null) {
		    				w.println(s);		    			
		    			}
			    }	    
		    }
		    else {
		    		w.println("Unknown project " + project);
		    }
		} catch (Exception exp) {
			exp.printStackTrace();
		}	
    }
	
	public void setJSoupHandler(JSoupHandler jsoupHandler) {
		this.jsoupHandler = jsoupHandler;
	}
}