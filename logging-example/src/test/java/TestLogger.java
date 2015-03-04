import org.junit.Test;

import java.util.logging.*;

public class TestLogger {

	@Test
	public void getLoggerAndHandlerLevels() {
		Logger testLogger = Logger.getLogger("TestLogger");
		System.out.println("Logger level:" + testLogger.getLevel());
		
		System.out.println("Parent name: " + testLogger.getParent().getName());
		System.out.println("Parent level: " + testLogger.getParent().getLevel());
		
		Handler handlers[] = testLogger.getHandlers();
		for(int i = 0; i<handlers.length; i++) {
			System.out.println("Handler level:" + handlers[i].getLevel());			
		}
		
		Handler pHandlers[] = testLogger.getParent().getHandlers();
		for(int i = 0; i<pHandlers.length; i++) {
			System.out.println("Parent Handler level:" + pHandlers[i].getLevel());			
		}		
	}
		
	@Test
	public void testPrintInfoMessage() {
		Logger testLogger = Logger.getLogger("TestLogger");		
		testLogger.info("This is a info message");
	}
	
	@Test
	public void testPrintConfigMessageFailure() {
		Logger testLogger = Logger.getLogger("TestLogger");		
		testLogger.config("This is a config message");
	}	
	
	@Test
	public void testPrintConfigMessageLoggerLevelSet() {
		Logger testLogger = Logger.getLogger("TestLogger");
		testLogger.setLevel(Level.CONFIG);
		testLogger.config("This is a config message");
	}	

	@Test
	public void testPrintConfigMessageLoggerAndHandlerLevelSet() {
		Logger testLogger = Logger.getLogger("TestLogger");
		testLogger.setLevel(Level.CONFIG);
		
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.CONFIG);
		
		testLogger.addHandler(consoleHandler);
		testLogger.config("This is a config message");
	}	

	@Test
	public void testPrintFineMessage() {
		Logger testLogger = Logger.getLogger("TestLogger");		
		testLogger.fine("This is a finest level message");
	}
	
	@Test
	public void testPrintFinerMessage() {
		Logger testLogger = Logger.getLogger("TestLogger");		
		testLogger.finer("This is a finest level message");
	}

	@Test
	public void testPrintFinestMessage() {
		Logger testLogger = Logger.getLogger("TestLogger");		
		testLogger.finest("This is a finest level message");
	}
	
	@Test
	public void testPrintWarningMessage() {
		Logger testLogger = Logger.getLogger("TestLogger");		
		testLogger.warning("This is a warning level message");
	}

	@Test
	public void testPrintSevereMessage() {
		Logger testLogger = Logger.getLogger("TestLogger");		
		testLogger.severe("This is a severe level message");
	}
}