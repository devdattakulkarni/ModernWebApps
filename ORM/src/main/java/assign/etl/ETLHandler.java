package assign.etl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import assign.domain.Assignment;

public class ETLHandler {
	private SessionFactory sessionFactory;
	
	public ETLHandler() {
		// A SessionFactory is set up once for an application
        sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
	}
	
	public Long addAssignment(String title) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Assignment newAssignment = new Assignment( title, new Date() ); 
		session.save(newAssignment);
		Long assignmentId = newAssignment.getId();
		session.getTransaction().commit();
		session.close();
		return assignmentId;
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
