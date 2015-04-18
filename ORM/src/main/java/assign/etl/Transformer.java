package assign.etl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

public class Transformer {
	
	Logger logger;
	
	public Transformer() {
		logger = Logger.getLogger("Transformer");		
	}
	
	public Map<String, List<String>> transform(Map<String, List<String>> data) {
		// Read the data;
		// transform it as required;
		// return the transformed data;
		
		logger.info("Inside transform.");

		Map<String, List<String>> newData = new HashMap<String, List<String>>();

		// transform data into newData
		
		return newData;
	}
}