/*
The MIT License (MIT)

Copyright (c) 2015 Renjith Mathew

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.github.e2point718.har2jmx;

import java.io.IOException;

import org.json.JSONException;

import junit.framework.TestCase;

public class HARBuildTest extends TestCase {

	public void testInvalidJSON(){
		try {
			HAR.build("test");
			fail("Invalid HAR JSON object should throw JSONException");
		} catch (JSONException e) {			
		}
	}

	public void testInvalidJSONWithoutLog(){
		try {
			HAR.build("{}");
			fail("Invalid HAR JSON object should throw JSONException");
		} catch (JSONException e) {			
		}
	}
	
	public void testJSONWithoutPagesOrEntries(){
		assertNotNull(HAR.build("{\"log\": {}}"));
	}
	
	public void test_har_simple_get() throws IOException{
		HAR har = HAR.build(HARBuildTest.class.getResource("/har_simple_get.json"));
		assertNotNull(har);
		assertEquals(1, har.pages.length);
		assertEquals(18, har.entries.length);
		assertNotNull(har.entries[0].pageref);		
	}
	
	public void test_har_simple_post() throws IOException{
		HAR har = HAR.build(HARBuildTest.class.getResource("/har_simple_post.json"));
		System.out.println(har);		
	}
}
