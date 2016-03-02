package assign.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "course")
public class Course {
	
	private String name;
	private String department;
	
	public String getName() {
		return name;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}