import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class EnglishEditorServiceImpl implements EditorService {

	String name = "EnglishEditor";
	URL eavesdropURL = null;
	String baseURL = "http://eavesdrop.openstack.org/";
	
	public EnglishEditorServiceImpl() {
		try {
			eavesdropURL = new URL(baseURL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String composeEmail() {
		return "Hello world.";
	}

	public String getName() {
		return this.name;
	}

	public String getResponseFromEavesDrop(String projectName) {
		String retVal = "";		
		baseURL += "/meetings/" + projectName;		
		try {			
			eavesdropURL = new URL(baseURL);
			URLConnection connection = eavesdropURL.openConnection();
			String readData = readDataFromEavesdrop(connection);
			retVal = parseOutput(readData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	protected String readDataFromEavesdrop(URLConnection connection) {
		String retVal = "";
		try {
		BufferedReader in = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));

		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			retVal += inputLine;
		}
		in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	protected String parseOutput(String inputString) {
		String retVal = "";
		
		return retVal;
	}

	public void thisIsVoidFunction() {
		// TODO Auto-generated method stub
		
	}
}
