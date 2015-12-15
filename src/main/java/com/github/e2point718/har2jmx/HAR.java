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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class includes the attributes from the HAR spec that are relevant for JMX files
 * https://dvcs.w3.org/hg/webperf/raw-file/tip/specs/HAR/Overview.html
 * TODO remove this class (populate reflectively or directly build JMX from JSON)
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
		
		@Override
		public String toString() {
			return "Page [id=" + id + ", title=" + title + "]";
		}		
	}

	static final class Entry {
		
		Page pageref;
		Request request;
		Response response;
		
		@Override
		public String toString() {
			return "Entry [pageref=" + pageref + ",\n request=" + request + ",\n response=" + response + "]";
		}
				
	}
	
	static final class Request {
		
		Method method;
		URL url;
		
		String httpVersion;
		long headersSize;
		long bodySize;
		
		Cookie[] cookies;
		
		Map<String,String> headers;
		Map<String,String> queryString;
		
		PostData postData;

		@Override
		public String toString() {
			return "Request [method=" + method + ", url=" + url + ", httpVersion=" + httpVersion + ", headersSize="
					+ headersSize + ", bodySize=" + bodySize + ", cookies=" + Arrays.toString(cookies) + ", headers="
					+ headers + ", queryString=" + queryString + ", postData=" + postData + "]";
		}
		
		
	}
	
	

	static final class Response {
		int status;
		String statusText;
		String httpVersion;
		long headersSize;
		long bodySize;
		
		Cookie[] cookies;
		
		Map<String,String> headers;
		ResponseContent content;

		@Override
		public String toString() {
			return "Response [status=" + status + ", statusText=" + statusText + ", httpVersion=" + httpVersion
					+ ", headersSize=" + headersSize + ", bodySize=" + bodySize + ", cookies="
					+ Arrays.toString(cookies) + ", headers=" + headers + ",\n content=" + content + "\n]";
		}
	}
	
	static final class ResponseContent {
		long size;
		String mimeType;
		String text;
		@Override
		public String toString() {
			return "ResponseContent [size=" + size + ", mimeType=" + mimeType + ", text=" + text + "]";
		}
		
	}
	
	static final class PostData {
		String mimeType;
		PostDataParam[] params;
		String text;
		@Override
		public String toString() {
			return "PostData [mimeType=" + mimeType + ", params=" + Arrays.toString(params) + ", text=" + text + "]";
		}
		
		
	}
	
	static final class PostDataParam {
		String name;
		String value;
		String fileName;
		String contentType;
		
		@Override
		public String toString() {
			return "PostDataParam [name=" + name + ", value=" + value + ", fileName=" + fileName + ", contentType="
					+ contentType + "]";
		}
		
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
		
		@Override
		public String toString() {
			return "Cookie [name=" + name + ", value=" + value + ", path=" + path + ", domain=" + domain + ", httpOnly="
					+ httpOnly + ", secure=" + secure + "]";
		}
		
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
	
	private static String getOptionalProperty(JSONObject obj,String key){
		try {
			return obj.getString(key);
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
		Map<String,Page> pages = new HashMap<>();
		if(jPages!=null){
			h.pages = new Page[jPages.length()];
			for(int i=0;i<jPages.length();i++){
				JSONObject jPage = jPages.getJSONObject(i);
				Page page = new Page();
				h.pages[i] = page;
				page.id = jPage.getString("id");
				page.title = jPage.getString("title");
				pages.put(page.id, page);
			}
			
		}
		JSONArray jEntries = getOptionalArray(log,"entries");
		if(jEntries!=null){
			h.entries = new Entry[jEntries.length()];
			for(int i=0;i<jEntries.length();i++){
				JSONObject jEntry = jEntries.getJSONObject(i);
				Entry entry = new Entry();
				h.entries[i] = entry;
				entry.pageref = pages.get(getOptionalProperty(jEntry,"pageref"));
				JSONObject jRequest = jEntry.getJSONObject("request");				
				Request request = new Request();
				entry.request = request;
				request.httpVersion = jRequest.getString("httpVersion");
				request.method = Method.valueOf(jRequest.getString("method"));
				try {
					request.url = new URL(jRequest.getString("url"));
				} catch (MalformedURLException e) {
					throw new JSONException(e.getMessage());
				} 
				JSONArray jHeaders = getOptionalArray(jRequest, "headers");
				if(jHeaders!=null){
					request.headers = new HashMap<>();
					for(int j=0;j<jHeaders.length();j++){
						JSONObject jHeader = jHeaders.getJSONObject(j);
						request.headers.put(jHeader.getString("name"), jHeader.getString("value"));
					}
				}
				JSONArray jQueryString = getOptionalArray(jRequest, "queryString");
				if(jQueryString!=null){
					request.queryString = new HashMap<>();
					for(int j=0;j<jQueryString.length();j++){
						JSONObject jQueryStringO = jQueryString.getJSONObject(j);
						request.queryString.put(jQueryStringO.getString("name"), jQueryStringO.getString("value"));
					}
				}
				JSONArray jCookies = getOptionalArray(jRequest, "cookies");
				if(jCookies!=null){
					request.cookies = new Cookie[jCookies.length()];
					for(int j=0;j<jCookies.length();j++){
						JSONObject jCookie = jCookies.getJSONObject(j);
						Cookie cookie = new Cookie();
						request.cookies[j] = cookie;
						cookie.name = jCookie.getString("name");
						cookie.value = jCookie.getString("value");
					}
				}
				
				JSONObject jPostData = getOptionalObject(jRequest,("postData"));
				if(jPostData!=null){
					PostData pd = new PostData();
					request.postData = pd;
					pd.mimeType = jPostData.getString("mimeType");
					pd.text = getOptionalProperty(jPostData, "text");
					
					JSONArray jPostParams = getOptionalArray(jPostData, "params");
					if(jPostParams!=null){
						pd.params = new PostDataParam[jPostParams.length()];
						for(int j=0;j<jPostParams.length();j++){
							PostDataParam pdp = new PostDataParam();
							JSONObject jPostDataParam = jPostParams.getJSONObject(j);
							pd.params[j] = pdp;
							pdp.name = getOptionalProperty(jPostDataParam, "name");
							pdp.value = getOptionalProperty(jPostDataParam, "value");
							pdp.contentType = getOptionalProperty(jPostDataParam, "contentType");
							pdp.fileName = getOptionalProperty(jPostDataParam, "fileName");
						}
					}
				}
				JSONObject jResponse = jEntry.getJSONObject("response");
				Response response = new Response();
				entry.response = response;
				response.httpVersion = jResponse.getString("httpVersion");
				response.status = jResponse.getInt("status");
				response.statusText = jResponse.getString("statusText");
				jHeaders = getOptionalArray(jResponse, "headers");
				if(jHeaders!=null){
					response.headers = new HashMap<>();
					for(int j=0;j<jHeaders.length();j++){
						JSONObject jHeader = jHeaders.getJSONObject(j);
						response.headers.put(jHeader.getString("name"), jHeader.getString("value"));
					}
				}
				jCookies = getOptionalArray(jResponse, "cookies");
				if(jCookies!=null){
					response.cookies = new Cookie[jCookies.length()];
					for(int j=0;j<jCookies.length();j++){
						JSONObject jCookie = jCookies.getJSONObject(j);
						Cookie cookie = new Cookie();
						response.cookies[j] = cookie;
						cookie.name = jCookie.getString("name");
						cookie.value = jCookie.getString("value");
					}
				}
				JSONObject jContent = getOptionalObject(jResponse,("content"));
				if(jContent!=null){
					ResponseContent content = new ResponseContent();
					response.content = content;
					content.size = jContent.getLong("size");
					content.mimeType =jContent.getString("mimeType");
					content.text = getOptionalProperty(jContent,("text"));
				}
			}
			
		}
		return h;
	}
	
	@Override
	public String toString() {
		return "HAR [pages=" + Arrays.toString(pages) + ",\n entries=" + Arrays.toString(entries) + "\n]";
	}
}
