import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * 
 */
public class JSoupParser {
	
	public static void main(String [] args) {
	
		try {
		    
			String source = "http://eavesdrop.openstack.org/irclogs/%23heat/";
		    Document doc = Jsoup.connect(source).get();
		    Elements links = doc.select("body a");
		    
		    ListIterator<Element> iter = links.listIterator();
		    while(iter.hasNext()) {
		    	Element e = (Element) iter.next();
		    	System.out.println("Element name:" + e.nodeName() + " Element:" + e.toString());
		    	String s = e.html();
		    	s = s.replace("#", "%23");
		    	System.out.println("Link:" + source + s);
		    }
		    
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}