package assign.etl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import assign.domain.Assignment;
import assign.domain.UTCourse;

import java.util.logging.*;

public class DBLoader {
	private SessionFactory sessionFactory;
	
	Logger logger;
	
	public DBLoader() {
		// A SessionFactory is set up once for an application
        sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
        
        logger = Logger.getLogger("EavesdropReader");
	}
	
	public void loadData(Map<String, List<String>> data) {
		logger.info("Inside loadData.");
	}
	
	public Long addAssignment(String title) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long assignmentId = null;
		try {
			tx = session.beginTransaction();
			Assignment newAssignment = new Assignment( title, new Date() ); 
			session.save(newAssignment);
		    assignmentId = newAssignment.getId();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();			
		}
		return assignmentId;
	}
	
	public Long addAssignmentAndCourse(String title, String courseTitle) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long assignmentId = null;
		try {
			tx = session.beginTransaction();
			Assignment newAssignment = new Assignment( title, new Date() );
			UTCourse course = new UTCourse(courseTitle);
			newAssignment.setCourse(course);
			session.save(course);
			session.save(newAssignment);
		    assignmentId = newAssignment.getId();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();			
		}
		return assignmentId;
	}
	
	public Long addAssignmentsToCourse(List<String> assignments, String courseTitle) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long courseId = null;
		try {
			tx = session.beginTransaction();
			UTCourse course = new UTCourse(courseTitle);
			session.save(course);
			courseId = course.getId();
			for(String a : assignments) {
				Assignment newAssignment = new Assignment( a, new Date() );
				newAssignment.setCourse(course);
				session.save(newAssignment);
			}
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();			
		}
		return courseId;
	}
	
	public List<Assignment> getAssignmentsForACourse(Long courseId) throws Exception {
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Assignment where course=" + courseId;		
		List<Assignment> assignments = session.createQuery(query).list();		
		return assignments;
	}
	
	public Assignment getAssignment(String title) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Assignment.class).
        		add(Restrictions.eq("title", title));
		
		List<Assignment> assignments = criteria.list();
		
		return assignments.get(0);		
	}
	
	public Assignment getAssignment(Long assignmentId) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Assignment.class).
        		add(Restrictions.eq("id", assignmentId));
		
		List<Assignment> assignments = criteria.list();
		
		return assignments.get(0);		
	}
}
