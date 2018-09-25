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

import com.jumkid.media.exception.MediaStoreServiceException;


public interface FileStorage<T> {

	/**
	 * Persist file in repository
	 * 
	 * @param bytes
	 * @throws Exception
	 */
	public T saveFile(byte[] bytes, T t) throws MediaStoreServiceException;

	/**
	 * Get file from repository
	 *
	 * @param t
	 * @return
	 * @throws MediaStoreServiceException
	 */
	public FileChannel getFile(T t) throws MediaStoreServiceException;
	
	/**
	 * Get file from repository with random accessing
	 * 
	 * @param t
	 * @return
	 * @throws MediaStoreServiceException
	 */
	public FileChannel getRandomAccessFile(T t) throws MediaStoreServiceException;

	/**
	 * Remove file from storage
	 *
	 * @param f
	 * @throws MediaStoreServiceException
	 */
	public void deleteFile(T f) throws MediaStoreServiceException;

	/**
	 * Get file thumbnail from repository
	 *
	 * @param t
	 * @param large
	 * @return
	 * @throws MediaStoreServiceException
	 */
	public FileChannel getThumbnail(T t, boolean large) throws MediaStoreServiceException;
}
