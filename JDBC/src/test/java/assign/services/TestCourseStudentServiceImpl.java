package assign.services;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import assign.domain.Course;

public class TestCourseStudentServiceImpl {
	
	CourseStudentService csService = null;
	
	@Before
	public void setUp() {
		String dburl = "jdbc:mysql://localhost:3306/student_courses";
		String dbusername = "devdatta";
		String dbpassword = "";
		csService = new CourseStudentServiceImpl(dburl, dbusername, dbpassword);
	}
	
	@Test
	public void testCourseAddition() {
		try {
			Course c = new Course();
			c.setName("Introduction to Computer Science.");
			c.setCourseNum("CS101");
			c = csService.addCourse(c);
			
			Course c1 = csService.getCourse(c.getCourseId());
			
			assertEquals(c1.getName(), c.getName());
			assertEquals(c1.getCourseNum(), c.getCourseNum());
			assertEquals(c1.getCourseId(), c.getCourseId());			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
