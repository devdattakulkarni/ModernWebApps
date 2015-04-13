package assign.etl;

import org.junit.Test;

import assign.domain.Assignment;
import junit.framework.TestCase;

public class TestDBWriter extends TestCase {

	ETLHandler dbWriter;
	
	@Override
	protected void setUp() {
		dbWriter = new ETLHandler();
	}
	
	@Test
	public void testAssignmentInsert() {
		try {
			String title = "HTTP Proxy Server";
			Long assignmentId = dbWriter.addAssignment(title);
			System.out.println("Assignment ID:" + assignmentId);
			
			Assignment proxyServer = dbWriter.getAssignment(title);
			assertEquals(proxyServer.getTitle(), title);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAssignmentGetWithId() {
		try {
			String title = "Servlets";
			Long assignmentId = dbWriter.addAssignment(title);
			System.out.println("Assignment ID:" + assignmentId);
			
			Assignment proxyServer = dbWriter.getAssignment(assignmentId);
			assertEquals(proxyServer.getTitle(), title);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
