package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "abc")
@XmlAccessorType
public class Courses {

    private List<Course> courseList = null;

    public List<Course> getCourseList() {
        return courseList;
    }
 
    public void setCourseList(List<Course> courses) {
        this.courseList = courses;
    }	
}
