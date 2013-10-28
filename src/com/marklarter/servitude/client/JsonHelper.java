package com.bi.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.bi.totalaccess.homevisit.HomeVisit;
import com.bi.totalaccess.homevisit.model.StringHelper;

public final class JsonHelper {
	
	public static Date DateFromString(String in) {
		Date result = null;
		if (!StringHelper.isNullOrWhitespace(in)) {
			try {
				result = new SimpleDateFormat(HomeVisit.DATE_FORMAT_PRIMARY, Locale.US).parse(in);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return result;
	}
	
}
