package assign.etl;

import org.junit.Test;

import assign.domain.Assignment;
import junit.framework.TestCase;

public class TestETLHandler extends TestCase {

	ETLHandler etlHandler;
	
	@Override
	protected void setUp() {
		etlHandler = new ETLHandler();
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
