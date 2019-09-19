import java.util.ArrayList;
import java.util.List;
 
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement(name = "response")
public class Response
{
    @XmlElement(name = "row")
    private List<Row> rowList = new ArrayList<Row>();
 
    public List<Row> getRowList() {
        return rowList;
    }
 
    public void setRow(List<Row> rowList) {
        this.rowList = rowList;
    }
}