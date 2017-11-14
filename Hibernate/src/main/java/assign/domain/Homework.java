package assign.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
//@Table
public class Homework {
	private Long id;

    private String title;
    private UTCourse utcourse; // course or something else
    
    public Homework() {
    	// this form used by Hibernate
    }
    
    public Homework(String title) {
    	// for application use, to create new homework
    	this.title = title;
    }
    
    @Id   
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
		return id;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }
    
    @ManyToOne
    @JoinColumn
    public UTCourse getCourse() { // property named course available on this object
    	return this.utcourse;
    }

    public void setCourse(UTCourse c) {
    	this.utcourse = c;
    }

    public String getTitle() {
		return title;
    }

    public void setTitle(String title) {
		this.title = title;
    }

}
