package assign.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import assign.domain.Course;
import assign.domain.Courses;
import assign.domain.Project;
import assign.domain.Projects;
import assign.services.EavesdropService;

@Path("/listing")
public class UTCoursesResource {
	
	@Inject
	private EavesdropService eavesdropService;
	
	public UTCoursesResource() {
		// Dependency Inject not used.
		//this.eavesdropService = new EavesdropService();
		//this.eavesdropService = eavesdropService;
	}
	
	public void setEavesdropService(EavesdropService eavesdropService) {
		this.eavesdropService = eavesdropService;
	}
	
	@GET
	@Produces("text/html")
	public String welcome() {
		return "Welcome message.";		
	}

	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		return "Hello world";		
	}
	
	@GET
	@Path("/helloeavesdrop")
	@Produces("text/html")
	public String helloEavesdrop() {
		return this.eavesdropService.getData();		
	}	
	
	@GET
	@Path("/courses")
	@Produces("application/xml")
	public StreamingOutput getAllCourses() throws Exception {
		Course modernWebApps = new Course();
		modernWebApps.setDepartment("CS");
		modernWebApps.setName("Modern Web Applications");
		
		Course operatingSystems = new Course();
		operatingSystems.setDepartment("CS");
		operatingSystems.setName("Operating Systems");
		
		final Courses courses = new Courses();
		List<Course> courseList = new ArrayList<Course>();
		courseList.add(modernWebApps);
		courseList.add(operatingSystems);
		courses.setCourses(courseList);		
			    
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	            outputCourses(outputStream, courses);
	         }
	      };	    
	}
	
	@GET
	@Path("/courses/{course_id}/")
	@Produces("application/xml")
	public Course getCourse(@PathParam("course_id") String course_id) {
		// This method uses resteasy's JAXB provider for marshalling the response
		System.out.println("Inside courses course_id:" + course_id);
		Course modernWebApps = new Course();
		modernWebApps.setDepartment("CS");
		if (course_id.equals("1")) {
			String data = this.eavesdropService.getData();
			modernWebApps.setName(data);
		} else {
			modernWebApps.setName("Modern Web Applications");			
		}

		return modernWebApps;
	}


	@GET
	@Path("/projects")
	@Produces("application/xml")
	public StreamingOutput getAllProjects() throws Exception {
		Project heat = new Project();
		heat.setName("%23heat");
				
		final Projects projects = new Projects();
		projects.setProjects(new ArrayList<String>());
		projects.getProjects().add("%23heat");
		projects.getProjects().add("%23dox");		
			    
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	            outputProjects(outputStream, projects);
	         }
	      };	    
	}	
	
	@GET
	@Path("/projects/{project_id}")
	@Produces("application/xml")
	public StreamingOutput getProject(@PathParam("project_id") String project_id) throws Exception {
		System.out.println("Inside projects project_id:" + project_id);
		final Project heat = new Project();
		heat.setName("%23heat");
		heat.setLink(new ArrayList<String>());
		heat.getLink().add("l1");
		heat.getLink().add("l2");		
			    
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	            outputProject(outputStream, heat);
	         }
	      };	    
	}		
	
	protected void outputCourses(OutputStream os, Courses courses) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Courses.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(courses, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
	protected void outputProjects(OutputStream os, Projects projects) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Projects.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(projects, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}	
	
	protected void outputProject(OutputStream os, Project project) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(project, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
}