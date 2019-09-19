import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "row")
public class Row {
	    private String cameraStatus;
	    private String ipCommStatus;
	    
	    @XmlElement(name="camera_status")
	    public String getCameraStatus() {
	    	return cameraStatus;
	    }
	    
	    public void setCameraStatus(String cameraStatus) {
	    	this.cameraStatus = cameraStatus;
	    }

	    @XmlElement(name="ip_comm_status")
	    public String getIpCommStatus() {
	    	return ipCommStatus;
	    }
	    
	    public void setIpCommStatus(String ipCommStatus) {
	    	this.ipCommStatus = ipCommStatus;
	    }
}
