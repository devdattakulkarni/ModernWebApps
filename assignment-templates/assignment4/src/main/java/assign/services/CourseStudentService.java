package assign.services;

import assign.domain.Course;
import assign.domain.NewCourse;

public interface CourseStudentService {

	public NewCourse addCourse(NewCourse c) throws Exception;
	
	public NewCourse getCourse(int courseId) throws Exception;

    public NewCourse getCourse_correct(int courseId) throws Exception;

}
