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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class includes the attributes from the HAR spec that are relevant for JMX files
 * https://dvcs.w3.org/hg/webperf/raw-file/tip/specs/HAR/Overview.html
 */
final class HAR implements Serializable {

	private static final long serialVersionUID = 8244089962694925306L;
	
	Page[] pages;
	Entry[] entries;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	static final class Page {
		String id;
		String title;
	}

	static final class Entry {
		
		Page page;
		Request request;
		Response response;
	}
	
	static final class Request {
		
		Page pageref;
		Method method;
		URL url;
		
		String httpVersion;
		long headersSize;
		long bodySize;
		
		Cookie[] cookies;
		
		Map<String,String> headers;
		Map<String,String> queryString;
		
		PostData postData;
	}
	
	

	static final class Response {
		
		String httpVersion;
		long headersSize;
		long bodySize;
		
		Cookie[] cookies;
		
		Map<String,String> headers;
		
	}
	
	static final class PostData {
		String mimeType;
		PostDataParam[] params;
		String text;
	}
	
	static final class PostDataParam {
		String name;
		String value;
		String fileName;
		String contentType;
	}

	static enum Method {
		GET, POST, PUT, DELETE /* TODO add the rest*/
	}
	
	static final class Cookie {
		String name;
		String value;
		String path;
		String domain;
		boolean httpOnly;
		boolean secure;
	}
	
	private static JSONArray getOptionalArray(JSONObject obj,String key){
		try {
			return obj.getJSONArray(key);
		} catch (JSONException e) {
			return null;
		}
	}
	
	private static JSONObject getOptionalObject(JSONObject obj,String key){
		try {
			return obj.getJSONObject(key);
		} catch (JSONException e) {
			return null;
		}
	}

	public static HAR build(URL source) throws IOException{
		String line = null;
		StringBuilder bu = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(source.openStream()));
		try{
			while((line=reader.readLine())!=null){
				bu.append(line);
			}
		}finally{
			reader.close();
		}
		return build(bu.toString());
	}
		
	public static HAR build(String source){
		HAR h = new HAR();
		JSONObject har = new JSONObject(source);
		JSONObject log = har.getJSONObject("log");
		JSONArray jPages = getOptionalArray(log,"pages");
		if(jPages!=null){
			h.pages = new Page[jPages.length()];
			
		}
		JSONArray jEntries = getOptionalArray(log,"entries");
		if(jEntries!=null){
			h.entries = new Entry[jEntries.length()];
		}
		return h;
	}
}
