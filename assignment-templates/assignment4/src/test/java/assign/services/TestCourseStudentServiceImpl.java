package assign.services;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import assign.domain.Course;
import assign.domain.NewCourse;

public class TestCourseStudentServiceImpl {
	
	CourseStudentService csService = null;
        Logger testLogger = Logger.getLogger("testlogger");
	
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
			NewCourse c = new NewCourse();
			c.setName("Introduction to Computer Science.");
			c.setCourseNum("CS102");
			c = csService.addCourse(c);
			
			NewCourse c1 = csService.getCourse(c.getCourseId());
			
			assertEquals(c1.getName(), c.getName());
			assertEquals(c1.getCourseNum(), c.getCourseNum());
			assertEquals(c1.getCourseId(), c.getCourseId());			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Test
	public void testCourseGet() {
	try {
	    NewCourse c = new NewCourse();
	    c.setName("Introduction to Computer Science.");
	    c.setCourseNum("CS102");
	    c = csService.addCourse(c);
	    
	    NewCourse c1 = csService.getCourse_correct(c.getCourseId());
	    testLogger.info(c1.getName());
	    testLogger.info(c1.getCourseNum());
	    testLogger.info(String.valueOf(c1.getCourseId()));
	    assertEquals(c1.getName(), c.getName());
	    assertEquals(c1.getCourseNum(), c.getCourseNum());
	    assertEquals(c1.getCourseId(), c.getCourseId());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
