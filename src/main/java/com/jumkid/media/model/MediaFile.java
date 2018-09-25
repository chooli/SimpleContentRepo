package com.jumkid.media.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

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
@Document(indexName = "mfile", type="mediafiles")
public class MediaFile {

	@Id
	@Field(type = FieldType.Keyword)
	private String id;

	protected String filename;
	
	protected String mimeType;
	
	@Field(type = FieldType.Long, store = true)
	protected Long size;
	
	protected String module;
		
	@Field(type = FieldType.Date, store = true)
	protected Date createdDate;
	
	protected String createdBy;
	
	protected String title;
	
	protected String content;
	
	protected String logicalPath;
	
	@Field(type = FieldType.Boolean)
	protected Boolean activated = false;
	
	protected String[] shareUsers;
	
	
	public MediaFile(){
		//void
	}
	
	public MediaFile(String id){
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLogicalPath() {
		return logicalPath;
	}

	public void setLogicalPath(String logicalPath) {
		this.logicalPath = logicalPath;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean isActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String[] getShareUsers() {
		return shareUsers;
	}

	public void setShareUsers(String[] shareUsers) {
		this.shareUsers = shareUsers;
	}

}
