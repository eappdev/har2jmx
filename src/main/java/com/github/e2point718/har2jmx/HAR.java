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

import java.io.Serializable;
import java.net.URL;
import java.util.Map;

import org.json.JSONObject;

/**
 * This class includes the attributes from the HAR spec that are relevant for JMX files
 * https://dvcs.w3.org/hg/webperf/raw-file/tip/specs/HAR/Overview.html
 */
public final class HAR implements Serializable {

	private static final long serialVersionUID = 8244089962694925306L;
	
	private String version;
	private Page[] pages;
	private Entry[] entries;
	
	static final class Log {
		
	}
	
	static final class Page {
		
	}

	static final class Entry {
		
		private Page page;
		private Request request;
		private Response response;
	}
	
	static final class Request {
		
		private Page page;
		private Method method;
		private URL url;
		private String httpVersion;
		
		private long headersSize;
		private long bodySize;
		
		
		private Map<String,String> headers;
		private Map<String,String> queryString;
	}
	
	

	static final class Response {
		
		
	}

	static enum Method {
		GET, POST, PUT, DELETE /* TODO add the rest*/
	}
	
	static final class Cookie {
		
	}
	
	public static HAR build(JSONObject har){
		HAR h = new HAR();
		return h;
	}
}
