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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.SetupThreadGroup;
import org.apache.jorphan.collections.HashTree;

import com.github.e2point718.har2jmx.HAR.Page;

class JMX {
	
	
	
	private List<HttpPage> pages;
	
	private static final class HttpPage {
		private final String pageId;
		private String title;
		public HttpPage(HAR.Page page) {
			super();
			this.pageId = page==null?UUID.randomUUID().toString():page.id;
			this.title = page==null?"":page.title;
		}
		private List<HTTPSampler> samples=new ArrayList<>();				
	}
	
	public static HashTree build(HashTree appendTo,String harIncoming){
		if(appendTo==null){
			appendTo = new HashTree();
			TestPlan testPlan = new TestPlan("Generated Test Plan");
			appendTo.add(testPlan);
			
			//${__P(host,localhost)}
	        
	        SetupThreadGroup testTG = new SetupThreadGroup();	        
	        testTG.setProperty(AbstractThreadGroup.NUM_THREADS, "${__P(,1)}");
	        testTG.setProperty(org.apache.jmeter.threads.ThreadGroup.RAMP_TIME, "${__P(,1)}");   
		}
		JMX jmx = build(harIncoming);
		for(HttpPage page:jmx.pages){
			
		}
		return appendTo;
	}
	
	private static JMX build(HAR har){
		JMX jmx = new JMX();
		jmx.pages = new ArrayList<>();
		Page pageTracking = null;
		for(HAR.Entry entry:har.entries){
			if(pageTracking==null||!pageTracking.equals(entry.pageref)){
				pageTracking=entry.pageref;
				/* new entry is added if page ref is missing or new */
				jmx.pages.add(new HttpPage(pageTracking));
			}
			HTTPSampler sampler = new HTTPSampler();
			sampler.setMethod(entry.request.method.name());			
			jmx.pages.get(jmx.pages.size()-1).samples.add(sampler);
		}
		return jmx;
	}
	
	private static JMX build(String har){		
		return build(HAR.build(har));
	}

}
