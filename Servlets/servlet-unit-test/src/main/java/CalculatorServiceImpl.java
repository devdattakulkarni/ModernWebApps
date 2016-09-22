
public class CalculatorServiceImpl implements CalculatorService {

	@Override
	public String calculate(String[] values, String operator) {
		String ret = "";
		if (values != null && operator != null) {
			long result = Long.parseLong(values[0]);
			for(int i=1; i<values.length; i++) {
				switch (operator) {
					case "add":
						result = result + Integer.parseInt(values[i]);
					case "subtract":
						result = result - Integer.parseInt(values[i]);
					case "multiply":
						result = result * Integer.parseInt(values[i]);
					case "divide":
						result = result / Integer.parseInt(values[i]);
				}
			}
			ret = String.valueOf(result);
		}
		else {
			return "Required parameters, values and operator, missing.";
		}
		return ret;	
	}

}
