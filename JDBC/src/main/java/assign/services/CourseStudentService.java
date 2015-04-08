package assign.services;

import assign.domain.Course;

public interface CourseStudentService {

	public Course addCourse(Course c) throws Exception;
	
	public Course getCourse(int courseId) throws Exception;
	
}
