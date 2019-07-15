package com.jumkid.media.repository;

/*
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 *
 * (c)2019 Jumkid Innovation All rights reserved.
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import com.jumkid.media.model.MediaFile;
import org.springframework.stereotype.Component;

@Component
public class FilePathManager {

	public static final String DELIMITER = "/";

	enum ReservedPath {
		TRASH("trash");

		private String value;

		ReservedPath(String value) { this.value = value; }

		public String getValue() { return value; }
	}

	public String getTrashPath() {
		return DELIMITER + ReservedPath.TRASH.value;
	}

	/**
	 * Use media file metadata to generate full storage path
	 *
	 * @param mfile the metadata of media file
	 * @return file full path
	 */
	public String getFullPath(MediaFile mfile) {
		return this.getCategoryPath(mfile.getMimeType()) + getLogicalPath() + DELIMITER + mfile.getId();
	}

	private String getLogicalPath(){
		//generate yyyymmdd string for today
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return DELIMITER + now.format(formatter);
	}
	
	private String getCategoryPath(String mimeType){
		return DELIMITER + mimeType.substring( 0, mimeType.indexOf(DELIMITER) );
	}

}
