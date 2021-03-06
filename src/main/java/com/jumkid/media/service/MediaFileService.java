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

import com.jumkid.media.model.MediaFile;
import com.jumkid.media.exception.MediaStoreServiceException;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

public interface MediaFileService {

    /**
     * Save media file and binary
     *
     * @param mFile file search info
     * @param file binary of file
     * @return MediaFile
     * @throws MediaStoreServiceException
     */
    MediaFile saveMediaFile(MediaFile mFile, byte[] file);

    /**
     * Retrieve media file by id
     *
     * @param id
     * @return
     * @throws MediaStoreServiceException
     */
    MediaFile getMediaFile(String id);

    /**
     * Retrieve media file source by id
     *
     * @param id media file identity
     * @return FileChannel
     * @throws MediaStoreServiceException
     */
    Optional<FileChannel> getSourceFile(String id);

    /**
     *
     * @param id media file identity
     */
    boolean deleteMediaFile(String id);

    /**
     * Get all media files
     *
     * @return List of mediaFile
     */
    List<MediaFile> getAll();

}
