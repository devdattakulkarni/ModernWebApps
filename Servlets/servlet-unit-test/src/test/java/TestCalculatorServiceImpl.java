import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestCalculatorServiceImpl {
	
	CalculatorService calculatorService;
	
	@Before
	public void setup() {
		calculatorService = new CalculatorServiceImpl();
	}
	
	@Test //1. Test when both operands are null
	public void testCalculateWhenBothOperandsNull() {
		String ret = calculatorService.calculate(null, null);
		assertEquals("Required parameters, values and operator, missing.", ret);
	}
	
	@Test //2. Test when when operator is null
	public void testCalculateWhenOperatorIsNull() {
		String ret = calculatorService.calculate(new String[1], null);
		assertEquals("Required parameters, values and operator, missing.", ret);		
	}
	
	@Test //3. Test when when values array is null
	public void testCalculateWhenValuesArrayIsNull() {
		String ret = calculatorService.calculate(null, "add");
		assertEquals("Required parameters, values and operator, missing.", ret);		
	}
	
	@Test //4. Test when only one value is passed in
	public void testAddWithOneValue() {
		String [] values = new String[1];
		values[0] = "1";
		String ret = calculatorService.calculate(values, "add");
		assertEquals("1", ret);
	}
	
	@Test //4. Test when only one value is passed in
	public void testAddWithtwoValues() {
		String [] values = new String[2];
		values[0] = "1";
		values[1] = "2";
		String ret = calculatorService.calculate(values, "add");
		assertEquals("3", ret);
	}
	
	@Test //5. Test when only two values are passed in
	public void testSubtractWithtwoValues() {
		String [] values = new String[2];
		values[0] = "1";
		values[1] = "1";
		String ret = calculatorService.calculate(values, "subtract");
		assertEquals("0", ret);
	}
	
	@Test //6. Test when only two value is passed in
	public void testMultiplyWithtwoValues() {
		String [] values = new String[2];
		values[0] = "3";
		values[1] = "2";
		String ret = calculatorService.calculate(values, "multiply");
		assertEquals("6", ret);
	}
	
	@Test //7. Test when only two value is passed in
	public void testMultiplyWithtwoValuesReverseOrder() {
		String [] values = new String[2];
		values[0] = "2";
		values[1] = "3";
		String ret = calculatorService.calculate(values, "multiply");
		assertEquals("6", ret);
	}
}
