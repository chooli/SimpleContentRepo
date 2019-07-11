package com.jumkid.media.service;

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

import java.util.Optional;

import com.jumkid.media.model.MediaFile;
import com.jumkid.media.repository.FileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service("fileService")
public class MediaFileServiceImpl implements MediaFileService {

	private static final Logger logger = LoggerFactory.getLogger(MediaFileServiceImpl.class);

	private FileStorage<MediaFile> fileStorage;

	@Autowired
	public MediaFileServiceImpl(FileStorage<MediaFile> esContentStorage) {
		this.fileStorage = esContentStorage;
	}

    @Override
    public MediaFile saveMediaFile(MediaFile mfile, byte[] file) {
        //TODO: extract meta data and content from office documentß
        mfile = fileStorage.saveFile(file, mfile);
        return mfile;
    }

	@Override
	public MediaFile getMediaFile(String id) {
    	logger.debug("Retrieve media file by given id {}", id);
		return fileStorage.getFile(id);
	}

    @SuppressWarnings("unchecked")
    @Override
    public Optional getSourceFile(String id) {
        logger.debug("Retrieve source file by given id {}", id);
        MediaFile mfile = fileStorage.getFile(id);
        return isRandomAccess(mfile) ? fileStorage.getRandomAccessFile(id) : fileStorage.getSourceFile(id);
    }

    @Override
    public boolean deleteMediaFile(String id) {
        return fileStorage.deleteFile(id);
    }

    private boolean isRandomAccess(MediaFile mFile){
        return mFile.getMimeType().startsWith("video") || mFile.getMimeType().startsWith("audio");
    }
	
}
