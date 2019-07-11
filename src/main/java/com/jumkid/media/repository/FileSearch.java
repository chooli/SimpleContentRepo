package com.jumkid.media.repository;

import com.jumkid.media.model.MediaFile;


/**
 * Created at Sep2018$
 *
 * @author chooliyip
 **/
public interface FileSearch {

    /**
     * find file by name
     *
     * @param filename
     * @return
     */
    MediaFile findByFilenameAndModule(String filename, String module);

}
