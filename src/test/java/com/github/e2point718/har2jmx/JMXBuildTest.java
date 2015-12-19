package com.github.e2point718.har2jmx;

import org.apache.jorphan.collections.HashTree;

import junit.framework.TestCase;

public class JMXBuildTest extends TestCase {

	public void testJMXBuild() throws Exception {
		HashTree tree = JMX.build(null, HAR.read(JMXBuildTest.class.getResource("/har_simple_get.json")));
		
	}
}
