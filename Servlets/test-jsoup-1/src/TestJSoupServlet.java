import java.io.IOException;
import java.io.PrintWriter;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestJSoupServlet {
	
	JSoupServlet jsoupServlet = null;
	
	@Before
	public void setUp() {
		jsoupServlet = new JSoupServlet();
	}
	
	@Test
	public void testMockedJSoupHandlerNoElements() throws ServletException, IOException {
		
		// Create mocks:
		JSoupHandler jsoupHandler = mock(JSoupHandler.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("project")).thenReturn("heat");
		jsoupServlet.setJSoupHandler(jsoupHandler);
		
		Elements list = mock(Elements.class);
		ListIterator<Element> iter = mock(ListIterator.class);
		Element e = mock(Element.class);
		
		// Set up expectations:
		
		String source = "http://eavesdrop.openstack.org/irclogs/heat";
		
		when(jsoupHandler.getElements(source)).thenReturn(list);		
		when(list.listIterator()).thenReturn(iter);
		when(iter.hasNext()).thenReturn(false);
		when(iter.next()).thenReturn(e);
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		PrintWriter pw = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(pw);
		
		// Invoke code under test:
		jsoupServlet.doGet(request, response);
		
		// Verify:
		verify(response, never()).getWriter();
		verify(pw, never()).println();
		
	}
	
	@Test
	public void testMockedJSoupHandlerOneElement() throws ServletException, IOException {
		
		// Create mocks:
		JSoupHandler jsoupHandler = mock(JSoupHandler.class);
		jsoupServlet.setJSoupHandler(jsoupHandler);
		
		Elements list = mock(Elements.class);
		ListIterator<Element> iter = mock(ListIterator.class);
		
		Element elem = mock(Element.class);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		// Set up expectations:
		when(jsoupHandler.getElements(anyString())).thenReturn(list);
		when(list.listIterator()).thenReturn(iter);
		when(iter.hasNext()).thenReturn(Boolean.TRUE).thenReturn(false);	
		when(iter.next()).thenReturn(elem);
		
		PrintWriter pw = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(pw);
		
		// Invoke code under test:
		jsoupServlet.doGet(request, response);
		
		// Verify:
		verify(response, times(1)).getWriter();
		
	}
	
	
	@Test
	public void testQueryHeatWorking() throws ServletException, IOException {
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		
		PrintWriter mockWriter = mock(PrintWriter.class);
		when(mockResponse.getWriter()).thenReturn(mockWriter);
		
		when(mockRequest.getParameter("project")).thenReturn("%23heat");		
		jsoupServlet.doGet(mockRequest, mockResponse);		
		verify(mockWriter, atLeastOnce()).println(anyString());
		
	}

}
