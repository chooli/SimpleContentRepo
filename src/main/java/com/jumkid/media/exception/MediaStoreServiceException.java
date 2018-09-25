package com.jumkid.media.exception;

import com.jumkid.media.model.MediaFile;

public class MediaStoreServiceException extends RuntimeException {

    MediaFile mfile;

    public MediaStoreServiceException(String errorMsg){
        super(errorMsg);
    }

    public MediaStoreServiceException(String errorMsg, MediaFile mfile){
        super(errorMsg);
        this.setMfile(mfile);
    }

    public MediaFile getMfile() {
        return mfile;
    }

    public void setMfile(MediaFile mfile) {
        this.mfile = mfile;
    }
	
}
