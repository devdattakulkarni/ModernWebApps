package assign.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "output")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotFound {

	String error;
		
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	} 
}
