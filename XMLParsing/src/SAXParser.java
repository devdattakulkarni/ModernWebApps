import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/*
 * Reference:
 * http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html 
 */
public class SAXParser extends DefaultHandler {
	
	public List<String> nodeValues = null;
	boolean assignmentNode = false;
	char vals[] = new char[100];
	int i=0;
	
	public static void main(String [] args) throws ParserConfigurationException, SAXException, IOException {
		
		if (args.length < 1) {
			System.out.println("Usage:");
			System.out.println("java SAXParser <XML File>");
			System.exit(0);
		}
		
		String fileName = args[0];
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
		javax.xml.parsers.SAXParser saxParser = spf.newSAXParser();
		
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(new SAXParser());
		xmlReader.parse(fileName);
	}
	
	public void startElement(String namespaceURI,
            String localName,
            String qName, 
            Attributes atts) throws SAXException {
		if (qName.equalsIgnoreCase("assignment")) {
			assignmentNode = true;
		}
	}
	
	public void endElement(String namespaceURI,
            String localName,
            String qName) throws SAXException {
		if (qName.equalsIgnoreCase("assignment")) {
			assignmentNode = false;
			vals[i] = '\0';
			i=0;
			String assignmentName = new String(vals);
			vals = new char[100];
			System.out.println("Node value:" + assignmentName);			
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (assignmentNode) {
			for(int j=start; j<start+length; j++, i++) {
				vals[i] = ch[j];
			}
		}
	}
	
	public void startDocument() throws SAXException {
		nodeValues = new ArrayList<String>();
    }
	
	public void endDocument() throws SAXException {
    }
}