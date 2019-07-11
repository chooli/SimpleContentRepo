package com.jumkid.media.repository;

/* 
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 *
 * (c)2013 Jumkid All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 3.0        Dec2013      chooli      creation
 * 
 *
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class FilePathManager {

	private final String yyyyMMdd = "yyyy/MM/dd";
	
	private final String yyyyMM = "yyyy/MM";
	
	private final String trashPath = "trash";

	/**
	 *
	 * @return
	 */
	public String getLogicalPath(){
		//format today date string
		SimpleDateFormat df = new SimpleDateFormat(yyyyMM);
        String timestemp = df.format(new Date());
        
        String currentPath = "/" + timestemp;
        return currentPath;
	}
	
	public String getCategoryPath(String mimeType){
		return "/"+mimeType.substring( 0, mimeType.indexOf("/") );
	}

	public String getTrashPath() {
		return trashPath;
	}

}
