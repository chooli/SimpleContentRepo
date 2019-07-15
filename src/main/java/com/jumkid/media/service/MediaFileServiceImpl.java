package com.jumkid.media.service;

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

import java.util.List;
import java.util.Optional;

import com.jumkid.media.model.MediaFile;
import com.jumkid.media.repository.FilePathManager;
import com.jumkid.media.repository.FileSearch;
import com.jumkid.media.repository.FileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service("fileService")
public class MediaFileServiceImpl implements MediaFileService {

	private static final Logger logger = LoggerFactory.getLogger(MediaFileServiceImpl.class);

	private FileSearch<MediaFile> fileSearch;

	private FileStorage<MediaFile> fileStorage;

	private FilePathManager filePathManager;

	@Autowired
	public MediaFileServiceImpl(FileSearch<MediaFile> esContentStorage, FileStorage<MediaFile> hdfsFileStorage,
                                FilePathManager filePathManager) {
        this.fileStorage = hdfsFileStorage;
	    this.fileSearch = esContentStorage;
	    this.filePathManager = filePathManager;
	}

    @Override
    public MediaFile saveMediaFile(MediaFile mfile, byte[] bytes) {

	    if(bytes == null) {
            return fileSearch.saveMetadata(mfile);
        } else {
            //firstly save metadata to get indexed doc with id
            mfile = fileSearch.saveMetadata(mfile);
            //second save file binary to file system
            mfile = fileStorage.saveFile(bytes, mfile);
            //finally update the logical path to metadata
            mfile = fileSearch.updateMetadata(mfile);

            return mfile;
        }

    }

	@Override
	public MediaFile getMediaFile(String id) {
    	logger.debug("Retrieve media file by given id {}", id);
		return fileSearch.getMetadata(id);
	}

    @SuppressWarnings("unchecked")
    @Override
    public Optional getSourceFile(String id) {
        logger.debug("Retrieve source file by given id {}", id);
        return fileSearch.getBinary(id);
    }

    @Override
    public boolean deleteMediaFile(String id) {
        MediaFile mfile = this.getMediaFile(id);
        if(mfile == null) {
            return false;
        }
        //remove metadata
	    if(fileSearch.deleteMetadata(id)) {
            //remove storage
            return fileStorage.deleteFile(filePathManager.getFullPath(mfile));
        }

	    return false;
    }

    @Override
    public List<MediaFile> getAll() {
        return fileSearch.getAll();
    }

    private boolean isRandomAccess(MediaFile mFile){
        return mFile.getMimeType().startsWith("video") || mFile.getMimeType().startsWith("audio");
    }
	
}
