import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JAXBExample {
	

	private static void unMarshalingExample() throws JAXBException, MalformedURLException, IOException
	{
	    JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	     
	    //We had written this file in marshalling example
	    Response cameraNodes = (Response) jaxbUnmarshaller.unmarshal( 
	    		new URL("http://www.cs.utexas.edu/~devdatta/traffic_camera_data.xml").openStream());
	    
	    for(Row camera : cameraNodes.getRowList())
	    {
	        System.out.print(camera.getCameraStatus());
	        System.out.println(camera.getIpCommStatus());
	    }
	}
	
	public static void main(String[] args) throws MalformedURLException, JAXBException, IOException {
		JAXBExample.unMarshalingExample();
	}
}
