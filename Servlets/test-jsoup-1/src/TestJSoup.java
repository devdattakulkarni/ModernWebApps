import java.io.IOException;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestJSoup extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
                    throws ServletException, IOException
    {
		response.getWriter().println("Hello world.");
		try {		    
			String source = "http://eavesdrop.openstack.org/irclogs/%23heat/";			
		    Document doc = Jsoup.connect(source).get();
		    Elements links = doc.select("body a");
		    
		    ListIterator<Element> iter = links.listIterator();
		    while(iter.hasNext()) {
		    		Element e = (Element) iter.next();
		    		String s = e.html();
		    		s = s.replace("#", "%23");
		    		response.getWriter().println(source + s);
		    }	    
		} catch (Exception exp) {
			exp.printStackTrace();
		}			
    }
}
