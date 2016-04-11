package assign.etl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

public class EavesdropReader {
	
	String url;
	Logger logger; 
	
	public EavesdropReader(String url) {
		this.url = url;
		
		logger = Logger.getLogger("EavesdropReader");
	}
	
	/*
	 * Return a map where the contents of map is a single entry:
	 * <this.url, List-of-parsed-entries>
	 */
	public Map<String, List<String>> readData() {
		
		logger.info("Inside readData.");
		
		Map<String, List<String>> data = new HashMap<String, List<String>>();
		
		// Read and parse data from this.url
				
		return data;
	}
}
