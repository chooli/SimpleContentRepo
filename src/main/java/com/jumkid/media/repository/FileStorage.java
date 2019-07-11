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
import java.nio.channels.FileChannel;
import java.util.Optional;

import com.jumkid.media.exception.MediaStoreServiceException;


public interface FileStorage<T> {

	/**
	 * Persist file in repository
	 * 
	 * @param bytes
	 * @param t
	 */
	T saveFile(byte[] bytes, T t);

	/**
	 * Get file from repository
	 *
	 * @param id identity of media
	 */
	T getFile(String id);

	/**
	 * Get file from repository
	 *
	 * @param id identity of media
	 * @return FileChannel
	 * @throws MediaStoreServiceException exception of media storage service
	 */
	Optional<byte[]> getSourceFile(String id);
	
	/**
	 * Get file from repository with the ability of random access
	 * 
	 * @param id identity of media
	 * @throws MediaStoreServiceException exception of media storage service
	 */
	Optional<FileChannel> getRandomAccessFile(String id);

	/**
	 * Remove file from storage
	 *
	 * @param id identity of media
	 * @throws MediaStoreServiceException exception of media storage service
	 */
	boolean deleteFile(String id);

	/**
	 * Get file thumbnail from repository
	 *
	 * @param id identity of media
	 * @param large
	 * @throws MediaStoreServiceException exception of media storage service
	 */
	Optional<FileChannel> getThumbnail(String id, boolean large);
}
