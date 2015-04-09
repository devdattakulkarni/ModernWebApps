package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "courses")
@XmlAccessorType
public class Courses {

	@XmlElement(name = "course")
    private List<Course> courses = null;
 
    public List<Course> getCourses() {
        return courses;
    }
 
    public void setEmployees(List<Course> courses) {
        this.courses = courses;
    }	
}
