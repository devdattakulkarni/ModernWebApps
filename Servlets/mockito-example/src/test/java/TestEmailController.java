import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestEmailController {
	
	EmailController emailController = new EmailController();
	EditorService mockEditor = null;
	
	@Before
	public void setUp1() {		
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
	public void testGetGreetingCorrectActionCompose() {
		when(mockEditor.composeEmail()).thenReturn("test email");
		String reply = emailController.getGreeting("compose");
		assertEquals("test email", reply);
		verify(mockEditor).thisIsVoidFunction();
	}
	
	@Test
	public void testGetGreeting1WithNullForEnglishEditor() {
		when(mockEditor.getName()).thenReturn(null);
		when(mockEditor.composeEmail()).thenReturn("test email");
		
		String reply = emailController.getGreeting1("compose");
		
		assertEquals("test email", reply);
		verify(mockEditor).thisIsVoidFunction();
	}
	
	@Test
	public void testGetGreeting1CorrectActionCompose() {
		when(mockEditor.getName()).thenReturn("EnglishEditor");
		when(mockEditor.composeEmail()).thenReturn("test email");
		
		String reply = emailController.getGreeting1("compose");
		
		assertEquals("test email", reply);
		verify(mockEditor).thisIsVoidFunction();
	}

	
	@Test
	public void testGetGreetingCorrectActionCreate() {
		when(mockEditor.composeEmail()).thenReturn("test email");
		String reply = emailController.getGreeting("create");
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
