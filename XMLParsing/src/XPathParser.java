import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/*
 * http://docs.oracle.com/javase/7/docs/api/javax/xml/xpath/package-summary.html
 */
public class XPathParser {
	
	public static void main(String [] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Usage:");
			System.out.println("java XPathParser <XML File>");
			System.exit(0);
		}
	
		String fileName = args[0];
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		String expression = "/html/body/cs378/assignments/assignment";
                //String expression = "response/meetings/a [href]";
                //String expression = "html/body/table/tr/a [href]";
		InputSource inputSource = new InputSource(fileName);
		NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
		
		for(int i=0; i<nodes.getLength(); i++) {
			Node n = nodes.item(i);
			System.out.println("Node value:" + n.getTextContent());
		}
	}
}
