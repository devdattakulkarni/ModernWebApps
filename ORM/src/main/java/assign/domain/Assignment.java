package assign.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "assignments" )
public class Assignment {
	
	private Long id;

    private String title;
    private Date date;
    private UTCourse utcourse; // course or something else
    
    public Assignment() {
    	// this form used by Hibernate
    }
    
    public Assignment(String title, Date date) {
    	// for application use, to create new events
    	this.title = title;
    	this.date = date;
    }
    
    public Assignment(String title, Date date, Long providedId) {
    	// for application use, to create new events
    	this.title = title;
    	this.date = date;
    	this.id = providedId;
    }    
    
    @Id    
	@GeneratedValue(generator="increment")    
	@GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
		return id;
    }

    private void setId(Long id) {
		this.id = id;
    }

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ASSIGNMENT_DATE")
    public Date getDate() {
		return date;
    }

    public void setDate(Date date) {
		this.date = date;
    }
    
    @ManyToOne
    @JoinColumn(name="course_id")
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
