package assign.services;

public class EavesdropService {

	public String getData() {
		return "Hello from Eavesdrop service.";
	}
	
	public String parseData(String input) {
		if (input != null) {
			return input;
		} else {
			return "";
		}
	}
}
