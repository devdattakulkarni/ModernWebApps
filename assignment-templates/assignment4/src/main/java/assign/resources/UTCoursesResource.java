package assign.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import assign.domain.Course;
import assign.domain.Courses;
import assign.domain.NotFound;
import assign.domain.Project;
import assign.domain.Projects;
import assign.services.CourseStudentService;
import assign.services.CourseStudentServiceImpl;

@Path("/listing")
public class UTCoursesResource {
	
	CourseStudentService courseStudentService;
	String password;
	String username;
	String dburl;	
	
	public UTCoursesResource(@Context ServletContext servletContext) {
		dburl = servletContext.getInitParameter("DBURL");
		username = servletContext.getInitParameter("DBUSERNAME");
		password = servletContext.getInitParameter("DBPASSWORD");
		this.courseStudentService = new CourseStudentServiceImpl(dburl, username, password);		
	}
	
	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		System.out.println("Inside helloworld");
		System.out.println("DB creds are:");
		System.out.println("DBURL:" + dburl);
		System.out.println("DBUsername:" + username);
		System.out.println("DBPassword:" + password);		
		return "Hello world " + dburl + " " + username + " " + password;		
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
	@Path("/projects")
	@Produces("application/xml")
	public StreamingOutput getAllProjects() throws Exception {
		//Project heat = new Project();
		//heat.setName("%23heat");
				
		final Projects projects = new Projects();
		projects.setProjects(new ArrayList<String>());
		//projects.getProjects().add("%23heat");
		projects.getProjects().add("%23dox");		
			    
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	            outputCourses(outputStream, projects);
	         }
	      };	    
	}	
	
	@GET
	@Path("/project")
	@Produces("application/xml")
	public StreamingOutput getProject() throws Exception {
		
		final Project heat = new Project();
		heat.setName("%23heat");
		heat.setLink(new ArrayList<String>());
		heat.getLink().add("l3");
		heat.getLink().add("l2");		
		
		//throw new WebApplicationException();
		
		final NotFound notFound = new NotFound();
		notFound.setError("Project non-existent-project does not exist");
		
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	            outputCourses(outputStream, notFound);
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
	
	protected void outputCourses(OutputStream os, Projects projects) throws IOException {
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
	
	protected void outputCourses(OutputStream os, Project project) throws IOException {
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
	
	protected void outputCourses(OutputStream os, NotFound notFound) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(NotFound.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(notFound, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}	
}
