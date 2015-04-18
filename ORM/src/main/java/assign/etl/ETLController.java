package assign.etl;

import java.util.List;
import java.util.Map;

public class ETLController {
	
	EavesdropReader reader;
	Transformer transformer;
	DBLoader loader;
	
	public ETLController() {
		transformer = new Transformer();
		loader = new DBLoader();
	}
	
	public static void main(String[] args) {
		ETLController etlController = new ETLController();
		etlController.performETLActions();
	}

	private void performETLActions() {		
		try {

			String source = "http://eavesdrop.openstack.org/meetings/solum/";
			reader = new EavesdropReader(source);
			
			// Read data
			Map<String, List<String>> data = reader.readData();
			
			// Transform data
			Map<String, List<String>> transformedData = transformer.transform(data);
			
			// Load data
			loader.loadData(transformedData);

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
