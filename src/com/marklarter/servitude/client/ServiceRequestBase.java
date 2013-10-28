package com.bi.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

/**
 * A base class for making REST service requests over HTTP. Supports optional JSON
 * request data. Expects derived class to provide custom processing for the response 
 * stream, including casting returned Object instance to a more specific class if desired.
 * Provides default implementations to read response stream into a String or into a binary 
 * file.
 * <p>
 * Example:
 * <pre>
 * private static class SomeServiceRequest extends ServiceRequestBase {
 * 
 *     public SomeServiceRequest() {
 *         super();
 *     }
 *   
 *  @Override
 *  protected File processResponseStream(InputStream responseStream)
 * 		throws IOException
 *  {
 *		String folderPath = Environment.getExternalStorageDirectory() + "/download/";
 *		String filespec = UUID.randomUUID().toString() + ".bin";
 *		
 *      return getFileFromStream(responseStream, folderPath, filespec);
 *  }
 *   
 * }
 * </pre>
 */
abstract class ServiceRequestBase {

	public ServiceRequestBase(boolean isHttps) {
		this.isHttps = isHttps;
		
		this.cookieManager = new CookieManager();
		CookieHandler.setDefault(this.cookieManager);
	}
    
    protected Object processRequest(String url, String requestMethod)
    	throws IOException
    {
    	return processRequest(url, requestMethod, null);
    }
    
    protected Object processRequest(String url, String requestMethod, JSONObject data)
    	throws IOException
    {
    	boolean doesInput = false;
    	OutputStream requestStream = null;
        InputStream responseStream = null;
        HttpURLConnection connection = null;
        Object result = null;
        if (requestMethod == METHOD_GET) {
    		
    	}
    	else if (requestMethod == METHOD_POST) {
    		doesInput = true;
    	}
    	else if (requestMethod == METHOD_PUT) {
    		doesInput = true;
    	}
    	else if (requestMethod == METHOD_DELETE) {
    		doesInput = true;
    	}
    	else {
    		throw new IOException("Unsupported request method");
    	}
    	
    	// Create connection.
    	try {
	        connection = (HttpURLConnection)(new URL(url).openConnection());
	        connection.setConnectTimeout(CONNECTION_CONNECT_TIMEOUT);
	    	connection.setReadTimeout(CONNECTION_READ_TIMEOUT);
	        if (this.isHttps) {
	        	// TODO: Setup specific HTTPS stuff needed to work on < Android 4.1.
			}
	    	
	    	// Set flags and headers.
	    	connection.setRequestMethod(requestMethod);
    		connection.setRequestProperty("Accept", "application/json");
    		connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
    		connection.setRequestProperty("X-Requested-With", "com.marklarter.servitude.client.JsonServiceHelper");
	    	if (doesInput) {
		    	connection.setDoOutput(true);
				connection.setDoInput(true);
	    	}
	    	
	    	// Connect.
	    	connection.connect();
	    	
	    	// Send data.
	    	if (doesInput && data != null) {
	    		requestStream = new BufferedOutputStream(connection.getOutputStream());
	    		requestStream.write(data.toString().getBytes());
	    		requestStream.flush();
	    	}
	    	
	    	// Receive and process result.
	    	responseStream = connection.getInputStream();
	    	if (responseStream != null) {
	    		result = processResponseStream(responseStream);
	    	}
    	}
    	catch (Exception ex) {
			ex.printStackTrace();
    	}
    	finally {
    		if (requestStream != null) {
    			requestStream.close();
    		}
    		if (responseStream != null) {
    			responseStream.close();
    		}
    		if (connection != null) {
    			connection.disconnect();
    		}
    	}
    	
        return result;
    }
    
    protected String getStringFromStream(InputStream responseStream)
    	throws IOException
    {
    	StringBuilder resultBuilder = new StringBuilder();
    	if (responseStream != null) {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
	        String line = "";
	        while ((line = reader.readLine()) != null) {
	            resultBuilder.append(line);
	        }
			reader.close();
    	}
    	
        return resultBuilder.toString();
    }
    
    protected File getFileFromStream(InputStream responseStream, File folder, String filespec)
    	throws IOException
    {
    	folder.mkdirs();
		File file = new File(folder, filespec);
		FileOutputStream fileStream = new FileOutputStream(file);

		byte[] buffer = new byte[1024];
		int readSize = 0;
		while ((readSize = responseStream.read(buffer)) != -1) {
			fileStream.write(buffer, 0, readSize);
		}
		fileStream.flush();
		fileStream.close();
    	
    	return file;
    }
    
    /**
     * Expects derived class to provide custom processing of the response stream,
     * including casting return Object instance to a more specific class if desired.
     */
    abstract Object processResponseStream(InputStream resultStream) throws IOException;
    
    protected static final String METHOD_GET = "GET";
    protected static final String METHOD_POST = "POST";
    protected static final String METHOD_PUT = "PUT";
    protected static final String METHOD_DELETE = "DELETE";
    
    protected static final int CONNECTION_CONNECT_TIMEOUT = 15000;
    protected static final int CONNECTION_READ_TIMEOUT = 30000;
    
    protected final CookieManager cookieManager;
    
    protected final boolean isHttps;

}
