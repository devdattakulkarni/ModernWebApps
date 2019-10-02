package assign.resources;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import assign.domain.Course;
import assign.services.EavesdropService;

public class TestUTCoursesResource {
	
	@Test
	public void unitTest1() {
		UTCoursesResource utCourseResource = new UTCoursesResource();
		EavesdropService mockEavesdrop = mock(EavesdropService.class);
		utCourseResource.setEavesdropService(mockEavesdrop);
		when(mockEavesdrop.getData()).thenReturn(null);
		
		Course c = utCourseResource.getCourse("1");
		assertEquals(null, c.getName());
	}
	
	@Test
	public void functionalTest1() {
		
	}

}
