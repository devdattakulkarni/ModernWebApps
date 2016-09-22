import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestHelloServlet {
	
	HelloServlet helloServlet;
	
	@Before
	public void setup() {
		helloServlet = new HelloServlet();
	}
	
	@Test
	public void testdoGet() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		PrintWriter writer = mock(PrintWriter.class);		
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getWriter()).thenReturn(writer);
		helloServlet.doGet(request, response);
		verify(writer).println("Hello world.");		
		verify(response).getWriter();
	}
	
	@Test
	public void testdoGet2() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		PrintWriter writer = mock(PrintWriter.class);		
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getParameter("username")).thenReturn("user1");
		when(response.getWriter()).thenReturn(writer);
		helloServlet.doGet2(request, response);
		verify(response).getWriter();
		verify(writer).println("Hello user1");
	}
	
	@Test
	public void testdoGet2NullUsername() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		PrintWriter writer = mock(PrintWriter.class);		
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getParameter("username")).thenReturn(null);
		when(response.getWriter()).thenReturn(writer);
		helloServlet.doGet2(request, response);
		verify(response).getWriter();
		verify(writer).println("Hello world.");		
	}

	@Test
	public void testdoGet3CheckCookie() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getParameter("username")).thenReturn("user1");
		helloServlet.doGet3(request, response);
		verify(response).addCookie(any(Cookie.class));
		
		// Note that following does not work.
		//Cookie c = new Cookie("user1", "user1");
		//verify(response).addCookie(c);
	}
	
	@Test
	public void testdoGet4() throws Exception {
		JSoupService mockJSoupService = mock(JSoupService.class);
		helloServlet.setJsoupservice(mockJSoupService);
		when(mockJSoupService.getElements()).thenReturn(null);
		int count = helloServlet.doGet4();
		assertEquals(0, count);
	}
}
