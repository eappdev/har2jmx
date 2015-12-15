package com.github.e2point718.har2jmx;

import java.io.File;
import java.util.UUID;

import junit.framework.TestCase;

public class CommandLineTest extends TestCase {

	public void testNoOptions(){
		try {
			HAR2JMX.main(new String[]{});
			fail("Missing required option: jmeterHome error is expected");
		} catch (Exception e) {			
		}
	}
	
	public void testInvalidJmeterHomeInvalidFile(){
		try {
			HAR2JMX.main(new String[]{"-jmeterHome",UUID.randomUUID().toString()});	
			fail("runtime exception is expected due to invalid jmeter home");
		} catch (Exception e) {			
		}
	}
	
	public void testInvalidJmeterHomeValidFile(){
		try {
			HAR2JMX.main(new String[]{"-jmeterHome",new File("").getAbsolutePath()});	
			fail("runtime exception is expected due to invalid jmeter home");
		} catch (Exception e) {			
		}
	}
}
