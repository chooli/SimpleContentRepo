package com.jumkid.media.repository;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import com.jumkid.media.model.MediaFile;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jumkid.media.exception.MediaStoreServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository("localFileStorage")
public class LocalFileStorageRepository {
	
	private final Log logger = LogFactory.getLog(this.getClass());

	@Value("${mediaserver.data.home}")
	private String dataHomePath;

	@Value("${mediaserver.thumbnail.small}")
	private int thumbnailSmall;

    @Value("${mediaserver.thumbnail.large}")
	private int thumbnailLarge;

	@Autowired
	private FilePathManager filePathManager;

	private enum ThumbnailInfo {
		SMALL_SUFFIX("_thmb"), LARGE_SUFFIX("_thmb_l"), EXTENSION(".png");

		String value;

		private ThumbnailInfo(String value) { this.value = value; }

		public String value() { return value; }
	}

	public MediaFile saveFile(byte[] bytes, MediaFile mfile) {
		
		if(bytes==null) return null;

		String logicalPath = filePathManager.getFullPath(mfile);

		try{
			mfile.setLogicalPath(logicalPath);

			Path dirPath = Paths.get(dataHomePath, logicalPath);
			Path path = Paths.get(dataHomePath, logicalPath, getFileUuid(bytes, mfile));

			SeekableByteChannel sbc = null;
			if(Files.exists(path)){   //replace the existing file if it exists
				sbc = Files.newByteChannel(path, StandardOpenOption.WRITE,
						StandardOpenOption.TRUNCATE_EXISTING);
			}else{
				if(!Files.exists(dirPath)) Files.createDirectories(dirPath);
				sbc = Files.newByteChannel(path, StandardOpenOption.WRITE,
						StandardOpenOption.CREATE_NEW);
			}

			try {
				sbc.write(ByteBuffer.wrap(bytes));

				//mfile.setSize(new Long(bytes.length));

			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally{
				sbc.close();
			}

			//generate thumbnail for image
			if(mfile.getMimeType().startsWith("image/")){
				generateThumbnail(path);
			}

			return mfile;

		} catch (FileAlreadyExistsException fae) {
			logger.error("file is already exists "+fae.getMessage());
		} catch(Exception e){
			throw new MediaStoreServiceException(e.getMessage());
		}
		
		return null;
		
	}

	public FileChannel getFile(MediaFile mfile) {
		try{
			Path path = Paths.get(dataHomePath, mfile.getLogicalPath(), mfile.getId());
			
			if (!Files.exists(path)) {
				logger.info("File "+path+" is not found.");
				return null;
			}
			@SuppressWarnings("resource")
			FileInputStream fin = new FileInputStream(new File(path.toString()));

			return fin.getChannel();
			
		}catch(Exception e){
			throw new MediaStoreServiceException(e.getMessage());
			//move to trash if 
		}
		
	}
	
	/**
	 * Get file from repository with random accessing
	 * 
	 * @param mfile
	 * @return
	 * @throws MediaStoreServiceException
	 */
	public FileChannel getRandomAccessFile(MediaFile mfile) {
		try{
			Path path = Paths.get(dataHomePath, mfile.getLogicalPath(), mfile.getId());
			
			if(!Files.exists(path)){
				logger.info("File "+path+" is not found.");
				return null;
			}
			@SuppressWarnings("resource")
			RandomAccessFile aFile = new RandomAccessFile(path.toString(), "rw");

			return aFile.getChannel();
			
		}catch(Exception e){
			throw new MediaStoreServiceException(e.getMessage());
			//move to trash if 
		}
	}

	public void deleteFile(MediaFile mfile) {
		Path path = Paths.get(dataHomePath, mfile.getLogicalPath(), mfile.getId());
		try {

			if(Files.deleteIfExists(path)){
				deleteThumbnail(mfile);
			}
			
		} catch(IOException e) {
			if(!Files.exists(path)) return;
			logger.warn("Failed to remove file "+path);
			this.moveToTrash(path);
		}
	}
	
	public FileChannel getThumbnail(MediaFile mfile, boolean large) {
		try{
			String logicalPath = mfile.getLogicalPath();
			String filePath = null;
			if(mfile.getMimeType().startsWith("image")) {
				filePath = dataHomePath + logicalPath + "/" + mfile.getId() +
							(large ? ThumbnailInfo.LARGE_SUFFIX : ThumbnailInfo.SMALL_SUFFIX) + ThumbnailInfo.EXTENSION;
			} else if(mfile.getMimeType().startsWith("video")) {
				filePath = dataHomePath + "/misc/icon_video.png";
			} else if(mfile.getMimeType().startsWith("audio")) {
				filePath = dataHomePath + "/misc/icon_audio.png";
			} else if(mfile.getMimeType().equals("application/pdf")) {
				filePath = dataHomePath + "/misc/icon_pdf.png";
			} else if(mfile.getMimeType().indexOf("mspowerpoint")!=-1) {
				filePath = dataHomePath + "/misc/icon_ppt.png";
			} else if(mfile.getMimeType().indexOf("msexcel")!=-1) {
				filePath = dataHomePath + "/misc/icon_xls.png";
			} else if(mfile.getMimeType().indexOf("msword")!=-1) {
				filePath = dataHomePath + "/misc/icon_doc.png";
			} else if(mfile.getMimeType().indexOf("avatar")!=-1) {
				filePath = dataHomePath + "/misc/icon_avatar.png";
			} else {
				filePath = dataHomePath + "/misc/icon_file.png";
			}
			
			File file = new File(filePath);
			if(!file.exists()) {
				logger.info("File in "+filePath+" is not found.");
				return null;
			}
			@SuppressWarnings("resource")
			FileInputStream fin = new FileInputStream(file);
			
			return fin.getChannel();
		}catch(Exception e){
			throw new MediaStoreServiceException(e.getMessage());
		}
		
	}
	
	private void generateThumbnail(Path filePath) throws IOException {
		String path = filePath.toString();
		
		Thumbnails.of(new File(path))
				.size(thumbnailSmall, thumbnailSmall)
				.outputFormat("PNG")
				.toFile(new File(path + ThumbnailInfo.SMALL_SUFFIX));
		
		Thumbnails.of(new File(path))
				.size(thumbnailLarge, thumbnailLarge)
				.outputFormat("PNG")
				.toFile(new File(path + ThumbnailInfo.LARGE_SUFFIX));
		
	}
	
	private void deleteThumbnail(MediaFile mfile) {
		
		if(mfile.getMimeType().startsWith("image")){
			Path path_s = getThumbnailPath(mfile, ThumbnailInfo.SMALL_SUFFIX.value() + ThumbnailInfo.EXTENSION.value());
			Path path_l = getThumbnailPath(mfile, ThumbnailInfo.LARGE_SUFFIX.value() + ThumbnailInfo.EXTENSION.value());
			
			try{

				Files.deleteIfExists(path_s);
				Files.deleteIfExists(path_l);
				
			}catch(IOException e){
				if(Files.exists(path_s)) {
					this.moveToTrash(path_s);
				}
				if(Files.exists(path_l)) {
					this.moveToTrash(path_l);
				}
				logger.warn("Failed to remove file "+e.getMessage());
			}
			
		}
		
	}
	
	private Path getThumbnailPath(MediaFile mfile, String suffix){
		return Paths.get(dataHomePath, mfile.getLogicalPath(), mfile.getId() + suffix);
	}
	
	private void moveToTrash(Path filePath) throws MediaStoreServiceException{
		//move file to trash
		Path trashPath = Paths.get(dataHomePath, filePathManager.getTrashPath());
		try{
			Files.move(filePath, trashPath, StandardCopyOption.ATOMIC_MOVE);
		}catch(IOException ioe){
			throw new MediaStoreServiceException("Failed to remove file "+filePath);
		}
	}

	private String getFileUuid(byte[] bytes, MediaFile mfile){
	    if(mfile.getId()==null){
            mfile.setId(UUID.nameUUIDFromBytes(bytes).toString());
        }
	    return mfile.getId();
    }

	public String getDataHomePath() {
		return dataHomePath;
	}

	public int getThumbnailSmall() {
		return thumbnailSmall;
	}

	public int getThumbnailLarge() {
		return thumbnailLarge;
	}


}
