package com.jumkid.media.service;

import javax.servlet.http.HttpServletRequest;

import com.jumkid.media.model.MediaFile;
import com.jumkid.media.exception.MediaStoreServiceException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/* 
 * This software is written by SocialStudio and subject
 * to a contract between SocialStudio and its customer.
 *
 * This software stays property of SocialStudio unless differing
 * arrangements between SocialStudio and its customer apply.
 *
 *
 * (c)2013 SocialStudio All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 1.0        Dec2013      chooli      creation
 * 
 *
 */

public interface MediaFileService {

    /**
     * Save mediafile and binary
     *
     * @param mfile
     * @param file
     * @return
     * @throws MediaStoreServiceException
     */
    MediaFile saveMediaFile(MediaFile mfile, byte[] file) throws MediaStoreServiceException;

    /**
     * Retrieve media file by id
     *
     * @param id
     * @return
     * @throws MediaStoreServiceException
     */
    MediaFile getMediaFile(String id) throws MediaStoreServiceException;

    /**
     * Search file by given keyword
     *
     * @param keyword
     * @param start
     * @param limit
     * @return
     * @throws MediaStoreServiceException
     */
    Page<MediaFile> searchFile(String keyword, Integer start, Integer limit) throws MediaStoreServiceException;

    /**
     * Get all files with pagination
     *
     * @param start
     * @param limit
     * @return
     * @throws MediaStoreServiceException
     */
    Page<MediaFile> getAllFiles(Integer start, Integer limit) throws MediaStoreServiceException;
    
    MediaFile transformRequestToMediaFile(MultipartFile file, HttpServletRequest request);
    
}
