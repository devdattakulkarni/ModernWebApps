public class Base64EncodeDecode {
	
	/*
	 * Reference http://ykchee.blogspot.com/2014/03/base64-in-java-8-its-not-too-late-to.html
	 * 
	 */
	public void testJava8Base64Encode() throws Exception {
		  String usernamePassword = "Aladdin:open sesame";
		  byte[] randomBinaryData = usernamePassword.getBytes("UTF-8");
		 
		  java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
		  byte[] encoded = encoder.encode(randomBinaryData);
		  
		  String encodedString = new String(encoded);
		   
		  System.out.println("Encoded string:[" + encodedString + "]");
	 }
	
	public static void main(String [] args) {
		Base64EncodeDecode test = new Base64EncodeDecode();
		try {
			test.testJava8Base64Encode();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
