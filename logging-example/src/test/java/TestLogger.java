import org.junit.Test;

import java.util.logging.*;

/*
 * The logging messages are controlled by configuration file:
 *
 * Ubuntu:
 * /usr/lib/jvm/java-8-oracle/jre/lib/logging.properties
 *
 * MacOS:
 * /Library/Java/JavaVirtualMachines/<JDK Home>/Contents/Home/jre/lib/logging.properties
 *
 * This file defines default levels for all loggers and ConsoleHandler. This level is INFO
 *
 * In order to exercise some of the methods you will need to modify this level.
 *
 * You can also specify custom file by setting -D java.util.logging.config.file property in
 * program arguments in run configurations. E.g.:
   -D java.util.logging.config.file=/Users/devdatta.kulkarni/Documents/UT/ModernWebApps/logging-example/logging.properties
 *
 */

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
		testLogger.fine("This is a fine level message");
	}
	
	@Test
	public void testPrintFinerMessage() {
		Logger testLogger = Logger.getLogger("TestLogger");		
		testLogger.finer("This is a finer level message");
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