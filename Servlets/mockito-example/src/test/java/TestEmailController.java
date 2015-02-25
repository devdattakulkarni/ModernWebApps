import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestEmailController {
	
	EmailController emailController = new EmailController();
	EditorService mockEditor = null;
	
	@Before
	public void setUp() {		
		mockEditor = mock(EditorService.class);		
		emailController.setEditorService(mockEditor);
	}
	
	@Test
	public void thisAlwaysPasses() {
    }
	
	@Test
	public void testHelloWorld() {
		String reply = emailController.helloWorld();
		assertEquals("Hello world!", reply);
	}
		
	@Test
	public void testGetComposedEmail() {
		when(mockEditor.composeEmail()).thenReturn("test email");
		String composedEmail = emailController.getComposedEmail();
		assertEquals("test email", composedEmail);
		verify(mockEditor).composeEmail();
	}
	
	@Test
	public void testGetGreetingCorrectActionLower() {
		when(mockEditor.composeEmail()).thenReturn("test email");
		String reply = emailController.getGreeting("compose");
		assertEquals("test email", reply);
	}
	
	@Test
	public void testGetGreetingCorrectActionCapital() {
		when(mockEditor.composeEmail()).thenReturn("test email");
		String reply = emailController.getGreeting("COMPOSE");
		assertEquals("test email", reply);
	}
		
	@Test
	public void testGetGreetingIncorrectAction() {
		String reply = emailController.getGreeting("ajdfksdhj");
		assertEquals("", reply);
		verify(mockEditor, never()).composeEmail();
	}
	
	@Test
	public void testGetGreetingNullAction() {
		String reply = emailController.getGreeting(null);
		assertEquals("", reply);
		verify(mockEditor, never()).composeEmail();		
	}
	
	@Test
	public void testWithGhostObject() {
		when(mockEditor.composeEmail()).thenReturn("test email");		
		String reply = emailController.getWithGhostEditor();
		assertEquals("test email", reply);		
	}
}
