import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMParser {
	
	public static void main(String [] args) {
		
		if (args.length < 1) {
			System.out.println("Usage:");
			System.out.println("java DOMParser <XML File>");
			System.exit(0);
		}
		
		String fileName = args[0];
		
		DocumentBuilderFactory builderFactory =
		        DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();  
		}
		
		Document document = null;
		try {
			document = builder.parse(
		        new FileInputStream(fileName));
		} catch (SAXException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		Element rootElement = document.getDocumentElement();
		Queue<Element> q = new LinkedList<Element>();
		q.add(rootElement);
		
		while(!q.isEmpty()) {
			Element e = (Element) q.remove();			
			if (e.getNodeName().equals("assignment")) {
		    	String nodeValue = e.getTextContent();
		    	System.out.println("Node value:" + nodeValue);
		    }
			NodeList nodes = e.getChildNodes();
			for(int i=0; i<nodes.getLength(); i++) {
				  Node node = nodes.item(i);
				  if(node instanceof Element) {
					  q.add((Element) node);
				    }
				  }
			}
	}
}
