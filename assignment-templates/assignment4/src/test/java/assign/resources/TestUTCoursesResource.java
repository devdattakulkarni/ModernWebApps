package assign.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;


public class TestUTCoursesResource
{

   private HttpClient client;

   @Before
   public void initClient()
   {
	   client = HttpClientBuilder.create().build();
   }

   @Test
   public void testGetCourseListing() throws Exception
   {
      System.out.println("**** Testing GetCourseListing ***");
      
      String url = "http://localhost:8080/resteasy/ut/listing/courses";
      
      // Code snippet taken from https://www.mkyong.com/java/apache-httpclient-examples/
      HttpGet request = new HttpGet(url);
      HttpResponse response = client.execute(request);

      System.out.println("Response Code : "
              + response.getStatusLine().getStatusCode());

	  BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));
		
	  StringBuffer result = new StringBuffer();
	  String line = "";
	  while ((line = rd.readLine()) != null) {
		  result.append(line);
	  }

      System.out.println(result);
      
      // Parse XML result
      
      // Write asserts to verify the output
   }   
}
