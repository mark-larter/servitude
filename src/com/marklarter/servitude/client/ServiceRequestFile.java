package com.bi.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;

import android.os.Environment;

public class ServiceRequestFile
	extends
		ServiceRequestBase
{

	public ServiceRequestFile(String filespec, boolean isHttps) {
		super(isHttps);
		
		this.filespec = filespec;
	}

	public ServiceRequestFile(String filespec) {
		this(filespec, false);
	}
	
    public File getFile(String url) {
    	File result = null; 
        try {
        	result = makeRequest(url, METHOD_GET);
        }
        catch (Exception ex) {
			ex.printStackTrace();
        }
        
    	return result;
    }
    
    @Override
    protected File processResponseStream(InputStream responseStream)
    	throws IOException
    {
		File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/bi");
		
        return getFileFromStream(responseStream, folder, this.filespec);
    }
    
    private File makeRequest(String url, String requestMethod)
    	throws IOException
    {
    	return makeRequest(url, requestMethod, null);
    }
    
    private File makeRequest(String url, String requestMethod, JSONObject data)
    	throws IOException
    {
        return (File)super.processRequest(url, requestMethod, data);
    }
    
    protected String filespec;

}
