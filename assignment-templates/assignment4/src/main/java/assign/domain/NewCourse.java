package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class NewCourse {

	String name;
	String course_num;
	int course_id;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCourseNum() {
		return course_num;
	}
	
	public void setCourseNum(String course_num) {
		this.course_num = course_num;
	}
	
	public int getCourseId() {
		return course_id;
	}
	
	public void setCourseId(int course_id) {
		this.course_id = course_id;
	}
}