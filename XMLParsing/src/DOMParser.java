import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
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

		List<Data> dataList = new ArrayList<Data>();
		int[] counts = new int[12];
		for (int i=0; i<12; i++) {
		    counts[i] = 0;
		}

		Element rootElement = document.getDocumentElement();
		Queue<Element> q = new LinkedList<Element>();
		q.add(rootElement);

		    String cameraStatus = "";
		    String ipCommStatus = "";

		while(!q.isEmpty()) {

		    Element e = (Element) q.remove();

			if (e.getNodeName().equals("camera_status")) {
		    	String nodeValue = e.getTextContent();
		    	//System.out.println("Node value:" + nodeValue);
			cameraStatus = nodeValue;
		        }

			if (e.getNodeName().equals("ip_comm_status")) {
		    	String nodeValue = e.getTextContent();
		    	//System.out.println("Node value:" + nodeValue);
			ipCommStatus = nodeValue;
		        }

			if (cameraStatus != "" && ipCommStatus != "") {
			    Data d = new Data(cameraStatus, ipCommStatus);
			    dataList.add(d);
			    cameraStatus = "";
			    ipCommStatus = "";
			}

			NodeList nodes = e.getChildNodes();
			for(int i=0; i<nodes.getLength(); i++) {
				  Node node = nodes.item(i);
				  if(node instanceof Element) {
					  q.add((Element) node);
				    }
				  }
			}
		System.out.println("DataList Size:" + dataList.size());
		for (int i=0; i<dataList.size(); i++) {
	Data d = dataList.get(i);
	System.out.print(i + " Camera Status:" + d.cameraStatus + " ");
	System.out.println(" IP Comm Status:" + d.ipCommStatus);
	if (d.cameraStatus.equalsIgnoreCase("desired") && d.ipCommStatus.equalsIgnoreCase("online")) {
	    counts[0]++;
	}
	if (d.cameraStatus.equalsIgnoreCase("desired") && d.ipCommStatus.equalsIgnoreCase("offline")) {
	    counts[1]++;
	}
	if (d.cameraStatus.equalsIgnoreCase("desired") && d.ipCommStatus.equalsIgnoreCase("no communication")) {
	    counts[2]++;
	}
    }
		for (int i=0; i<12; i++) {
		    System.out.println(counts[i]);
		}
	}
}
