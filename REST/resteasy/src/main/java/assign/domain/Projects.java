package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "projects")
@XmlAccessorType(XmlAccessType.FIELD)
public class Projects {
	    
    private List<String> project = null;
    
    public List<String> getProjects() {
    	return project;
    }
    
    public void setProjects(List<String> projects) {
    	this.project = projects;
    }
}
