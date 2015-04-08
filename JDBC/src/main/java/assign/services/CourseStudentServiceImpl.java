package assign.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import assign.domain.Course;

public class CourseStudentServiceImpl implements CourseStudentService {

	String dbURL = "";
	DataSource ds;
	
	public CourseStudentServiceImpl() {
		// DB connection information would typically be read from a config file.
		dbURL = "jdbc:mysql://localhost:3306/student_courses";
		ds = setupDataSource(dbURL);
	}
	
	public DataSource setupDataSource(String connectURI) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername("devdatta");
        ds.setPassword("");
        ds.setUrl(connectURI);
        return ds;
    }
	
	public Course addCourse(Course c) throws Exception {
		Connection conn = ds.getConnection();
		
		String insert = "INSERT INTO courses(name, course_num) VALUES(?, ?)";
		PreparedStatement stmt = conn.prepareStatement(insert,
                Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, c.getName());
		stmt.setString(2, c.getCourseNum());
		
		int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating course failed, no rows affected.");
        }
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
        	c.setCourseId(generatedKeys.getInt(1));
        }
        else {
            throw new SQLException("Creating course failed, no ID obtained.");
        }
		return c;
	}

	public Course getCourse(int courseId) throws Exception {
		String query = "select * from courses where course_id=" + courseId;
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		ResultSet r = s.executeQuery();
		
		if (!r.next()) {
			return null;
		}
		
		Course c = new Course();
		c.setCourseNum(r.getString("course_num"));
		c.setName(r.getString("name"));
		c.setCourseId(r.getInt("course_id"));
		return c;
	}

}
