package com.bi.services;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;

public class ServiceRequestJson
	extends
		ServiceRequestBase
{

	public ServiceRequestJson(boolean isHttps) {
		super(isHttps);
	}
	
    public ServiceRequestJson() {
		this(false);
	}

	public String getJson(String url) {
    	String result = ""; 
        try {
        	result = makeRequest(url, METHOD_GET);
        }
        catch (Exception ex) {
			ex.printStackTrace();
        }
        
    	return result;
    }
    
    public String postJson(String url, JSONObject data) {
    	return sendJson(url, METHOD_POST, data);
    }
    
    public String putJson(String url, JSONObject data) {
    	return sendJson(url, METHOD_PUT, data);
    }
    
    @Override
    protected String processResponseStream(InputStream responseStream)
    	throws IOException
    {
        return getStringFromStream(responseStream);
    }
    
    private String makeRequest(String url, String requestMethod)
    	throws IOException
    {
    	return makeRequest(url, requestMethod, null);
    }
    
    private String makeRequest(String url, String requestMethod, JSONObject data)
    	throws IOException
    {
        return (String)super.processRequest(url, requestMethod, data);
    }
    
    private String sendJson(String url, String requestMethod, JSONObject data) {
    	String result = ""; 
        try {
        	result = makeRequest(url, requestMethod, data);
        }
        catch (Exception ex) {
			ex.printStackTrace();
        }
        
    	return result;
    }

}
