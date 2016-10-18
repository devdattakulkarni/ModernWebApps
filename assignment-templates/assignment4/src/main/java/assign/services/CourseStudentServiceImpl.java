package assign.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import assign.domain.Course;
import assign.domain.NewCourse;

public class CourseStudentServiceImpl implements CourseStudentService {

	String dbURL = "";
	String dbUsername = "";
	String dbPassword = "";
	DataSource ds;

	// DB connection information would typically be read from a config file.
	public CourseStudentServiceImpl(String dbUrl, String username, String password) {
		this.dbURL = dbUrl;
		this.dbUsername = username;
		this.dbPassword = password;
		
		ds = setupDataSource();
	}
	
	public DataSource setupDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername(this.dbUsername);
        ds.setPassword(this.dbPassword);
        ds.setUrl(this.dbURL);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        return ds;
    }
	
	public NewCourse addCourse(NewCourse c) throws Exception {
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
        
        // Close the connection
        conn.close();
        
		return c;
	}

	public NewCourse getCourse(int courseId) throws Exception {
		String query = "select * from courses where course_id=" + courseId;
		Connection conn = ds.getConnection();
		PreparedStatement s = conn.prepareStatement(query);
		ResultSet r = s.executeQuery();
		
		if (!r.next()) {
			return null;
		}
		
		NewCourse c = new NewCourse();
		c.setCourseNum(r.getString("course_num"));
		c.setName(r.getString("name"));
		c.setCourseId(r.getInt("course_id"));
		return c;
	}

    public NewCourse getCourse_correct(int courseId) throws Exception {
	String query = "select * from courses where course_id=?";
	Connection conn = ds.getConnection();
	PreparedStatement s = conn.prepareStatement(query);
	s.setString(1, String.valueOf(courseId));

	ResultSet r = s.executeQuery();
	
	if (!r.next()) {
	    return null;
	}
	
	NewCourse c = new NewCourse();
	c.setCourseNum(r.getString("course_num"));
	c.setName(r.getString("name"));
	c.setCourseId(r.getInt("course_id"));
	return c;
    }

}
