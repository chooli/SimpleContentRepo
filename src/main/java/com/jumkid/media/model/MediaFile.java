package com.jumkid.media.model;

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

import com.jumkid.media.util.Constants;

import java.util.Date;

public class MediaFile {

	public enum Fields {
		ID("id"), 
		TITLE("title"),
		FILENAME("filename"), 
		MIMETYPE("mimeType"),
		SIZE("size"),
		MODULE("module"),
		CREATED_DATE("createdDate"),
		CREATED_BY("createdBy"),
		CONTENT("content"),
		ACTIVATED("activated"),
		BLOB("blob");

		private final String value;

		Fields(String value) { this.value = value; }

		public String value() { return this.value; }

	}

	private String id;

	private String filename;

	private String mimeType;
	
	private Integer size;

	private String module;
		
	private Date createdDate;

	private String createdBy;

	private String title;

	private String content;

	private Boolean activated;

	private String logicalPath;
	
	private MediaFile(Builder builder){
		this.id = builder.id;
		this.filename = builder.filename;
		this.title = builder.title;
		this.module = builder.module;
		this.size = builder.size;
		this.mimeType = builder.mimeType;
		this.createdDate = builder.createdDate;
		this.createdBy = builder.createdBy;
		this.content = builder.content;
		this.activated = builder.activated;
	}

	public void setId(String id) { this.id = id; }

	public String getId() {
		return id;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getContent() {
		return content;
	}

	public String getModule() {
		return module;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public String getFilename() { return filename; }

	public String getTitle() {
		return title;
	}

	public Boolean isActivated() { return activated; }

	public Integer getSize() {
		return size;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public String getLogicalPath() {
		return logicalPath;
	}

	public void setLogicalPath(String logicalPath) {
		this.logicalPath = logicalPath;
	}

	public static class Builder{

		private String id;
		private String filename;
		private String mimeType;
		private Integer size;
		private String module = Constants.MODULE_MFILE;
		private Date createdDate = new Date();
		private String createdBy;
		private String title;
		private String content;
		private Boolean activated = Boolean.TRUE;

		public MediaFile build() { return new MediaFile(this); }

		public Builder id(String val) { id = val; return this; }
		public Builder filename(String val) { filename = val; return this; }
		public Builder mimeType(String val) { mimeType = val; return this; }
		public Builder size(Integer val) { size = val; return this; }
		public Builder module(String val) { module = val == null ? module : val; return this; }
		public Builder createdDate(Date val) { createdDate = val == null ? createdDate : val; return this; }
		public Builder createdBy(String val) { createdBy = val; return this; }
		public Builder title(String val) { title = val; return this; }
		public Builder content(String val) { content = val; return this; }
		public Builder activated(Boolean val) { activated = val == null ? activated : val; return this; }

	}
}
