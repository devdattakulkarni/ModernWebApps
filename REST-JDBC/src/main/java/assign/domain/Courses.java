package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "courses")
public class Courses {

    private List<Course> courses = null;
 
    public List<Course> getCourses() {
        return courses;
    }
 
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }	
}
