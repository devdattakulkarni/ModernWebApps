package assign.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "ut_courses" )
public class UTCourse {
	
	private Long id;
    private String courseName;
    private Set<Assignment> assignments;

    public UTCourse() {
    	// this form used by Hibernate
    }
    
    public UTCourse(String courseName) {
    	this.courseName = courseName;
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
    
    @Column(name="course")
    public String getCourseName() {
		return courseName;
    }

    public void setCourseName(String courseName) {
		this.courseName = courseName;
    }
    
    @OneToMany(mappedBy="course")
    @Cascade({CascadeType.DELETE})
    public Set<Assignment> getAssignments() {
    	return this.assignments;
    }
    
    public void setAssignments(Set<Assignment> assignments) {
    	this.assignments = assignments;
    }
}