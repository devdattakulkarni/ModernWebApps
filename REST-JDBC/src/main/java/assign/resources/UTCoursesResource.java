package assign.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import assign.domain.Course;
import assign.domain.Courses;
import assign.domain.NewCourse;
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
	String dbhost, dbname;
	public UTCoursesResource(@Context ServletContext servletContext) {
		dbhost = servletContext.getInitParameter("DBHOST");
		dbname = servletContext.getInitParameter("DBNAME");
		dburl = "jdbc:mysql://" + dbhost + ":3306/" + dbname;
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
	
	@POST
	@Consumes("application/xml")
	public Response createCustomer(InputStream is) throws Exception {
	      NewCourse newCourse = readNewCourse(is);
	      newCourse = this.courseStudentService.addCourse(newCourse);
	      return Response.created(URI.create("/courses/" + newCourse.getCourseId())).build();
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
	
	protected NewCourse readNewCourse(InputStream is) {
		      try {
		         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		         Document doc = builder.parse(is);
		         Element root = doc.getDocumentElement();
		         NewCourse course = new NewCourse();
		         NodeList nodes = root.getChildNodes();
		         for (int i = 0; i < nodes.getLength(); i++) {
		            Element element = (Element) nodes.item(i);
		            if (element.getTagName().equals("name")) {
		               course.setName(element.getTextContent());
		            }
		            else if (element.getTagName().equals("course_num")) {
		               course.setCourseNum(element.getTextContent());
		            }
		         }
		         return course;
		      }
		      catch (Exception e) {
		         throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		      }
		   }
}