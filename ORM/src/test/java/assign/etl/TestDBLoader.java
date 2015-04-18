package assign.etl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import assign.domain.Assignment;
import junit.framework.TestCase;

public class TestDBLoader extends TestCase {

	DBLoader etlHandler;
	
	@Override
	protected void setUp() {
		etlHandler = new DBLoader();
	}
	
	@Test
	public void testAssignmentInsert() {
		try {
			String title = "HTTP Proxy Server";
			Long assignmentId = etlHandler.addAssignment(title);
			System.out.println("Assignment ID:" + assignmentId);
			
			Assignment proxyServer = etlHandler.getAssignment(title);
			assertEquals(proxyServer.getTitle(), title);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public void testUpdate() {
	}
	
	@Test
	public void testAssignmentAndCourseInsert() {
		try {
			String title = "ETL";
			String courseTitle = "Modern Web Applications";
			Long assignmentId = etlHandler.addAssignmentAndCourse(title, courseTitle);
			System.out.println("Assignment ID:" + assignmentId);
			
			Assignment proxyServer = etlHandler.getAssignment(title);
			assertEquals(proxyServer.getTitle(), title);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMultipleAssignments() {
		try {
		List<String> assignments = new ArrayList<String>();
		assignments.add("Memory Subsystem");
		assignments.add("Device Drivers");
		String courseTitle = "Operating Systems";
		Long courseId = etlHandler.addAssignmentsToCourse(assignments, courseTitle);
		
		List<Assignment> a = etlHandler.getAssignmentsForACourse(courseId);
		
		System.out.println("Title: " + a.get(0).getTitle());
		System.out.println("Title: " + a.get(1).getTitle());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAssignmentGetWithId() {
		try {
			String title = "Servlets";
			Long assignmentId = etlHandler.addAssignment(title);
			System.out.println("Assignment ID:" + assignmentId);
			
			Assignment proxyServer = etlHandler.getAssignment(assignmentId);
			assertEquals(proxyServer.getTitle(), title);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
