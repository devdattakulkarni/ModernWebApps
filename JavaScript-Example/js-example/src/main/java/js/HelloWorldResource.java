package js;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/path")
public class HelloWorldResource {
	
	public HelloWorldResource() {
		
	}
	
	@GET
	@Path("/helloworld-resource")
	@Produces("text/html")
	public String helloWorld() {
		System.out.println("Inside helloworld");
		return "Hello world ";
	}
}
