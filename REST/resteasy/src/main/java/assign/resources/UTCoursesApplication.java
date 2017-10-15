package assign.resources;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import assign.services.EavesdropService;

@ApplicationPath("/ut")
public class UTCoursesApplication extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();
	
	public UTCoursesApplication() {		
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		classes.add(UTCoursesResource.class);
		return classes;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
	
}
