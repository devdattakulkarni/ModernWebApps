package assign.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import assign.domain.Course;
import assign.domain.Courses;
import assign.domain.NewCourse;
import assign.domain.NotFound;
import assign.services.CourseStudentService;
import assign.services.CourseStudentServiceImpl;

@Path("/")
public class UTCoursesResource {

	CourseStudentService courseStudentService;
	String password;
	String username;
	String dburl;
	String host;
	String dbname;

	public UTCoursesResource(@Context ServletContext servletContext) {
		host = servletContext.getInitParameter("DBHOST");
		dbname = servletContext.getInitParameter("DBNAME");
		username = servletContext.getInitParameter("DBUSERNAME");
		password = servletContext.getInitParameter("DBPASSWORD");
		//jdbc:mysql://localhost:3306/student_courses
        dburl = "jdbc:mysql://" + host + ":3306/" + dbname + "?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		this.courseStudentService = new CourseStudentServiceImpl(dburl, username, password);		
	}

	@GET
	@Path("/")
	@Produces("text/html")
	public String helloWorld() {
		System.out.println("DB creds are:");
		System.out.println("DBURL:" + dburl);
		System.out.println("DBUsername:" + username);
		System.out.println("DBPassword:" + password);		
		return "DB Creds:" + dburl + " " + username + " " + password + "\n";		
	}

	@POST
	@Path("/courses")
	@Produces("application/xml")
	public NewCourse addCourse(NewCourse courseToAdd) throws Exception {
		String name = courseToAdd.getName();
		String courseNum = courseToAdd.getCourseNum();
		System.out.println("Name:" + name + " CourseNum:" + courseNum);
		NewCourse newCourse = this.courseStudentService.addCourse(courseToAdd);
		return newCourse;
	}

	@GET
	@Path("/courses/{course_id}")
	@Produces("application/xml")
	public NewCourse getCourse(@PathParam("course_id") int courseId) throws Exception {
		NewCourse newCourse = this.courseStudentService.getCourse_correct(courseId);
		return newCourse;
	}
	
	@GET
	@Path("/courses")
	@Produces("application/xml")
	public NewCourse getCourseWithQueryParam(@QueryParam("course_id") int courseId) throws Exception {
		NewCourse newCourse = this.courseStudentService.getCourse_correct(courseId);
		return newCourse;
	}

	@PUT
	@Path("/courses/{courseId}")
	@Produces("application/xml")
	public NewCourse updateCourse(@PathParam("courseId") int courseId) throws Exception {
		System.out.println("Course to update id:" + courseId);
		NewCourse updatedCourse = new NewCourse(); 
		// Make call to Service to update the course
		// Update course
		return updatedCourse;
	}

	@DELETE
	@Path("/courses/{courseId}")
	public Response deleteCourse(@PathParam("courseId") int courseId) throws Exception {
		System.out.println("Course to delete id:" + courseId);
		// Make call to Service to update the course
		return Response.ok().build();
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
	
	protected void outputNotFound(OutputStream os, NotFound notFound) throws IOException {
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