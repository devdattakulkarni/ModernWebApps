import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * http://docs.oracle.com/javase/tutorial/essential/regex/test_harness.html
 */
public class RegexParser {
	
	public static void main(String [] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Usage:");
			System.out.println("java RegexParser <XML File>");
			System.exit(0);
		}
	
		String fileName = args[0];
		String fileContents = getFileContents(fileName);
		
		Pattern startPattern = Pattern.compile("<assignment>");
		Matcher startMatch = startPattern.matcher(fileContents);
						
		//<assignment>123</assignment>
		while(startMatch.find()) {
			int start = startMatch.end();			
			StringBuffer b = new StringBuffer();
			int i=start;
			while(fileContents.charAt(i) != '<') {
				b.append(fileContents.charAt(i));
				i++;
			}
			b.append('\0');
			System.out.println("Node value:" + b.toString());			
		}		
	}
	
	private static String getFileContents(String file) throws Exception {
		File f = new File(file);
		InputStream in = new FileInputStream(f);
		InputStreamReader i = new InputStreamReader(in);
		BufferedReader bf = new BufferedReader(i);		
		String fileContents = "";
		String line = null;
		while ((line = bf.readLine()) != null) {
			fileContents += line;
		}
		bf.close();		
		return fileContents;
	}
}
