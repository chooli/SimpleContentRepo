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

import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.jumkid.media.model.MediaFile;
import com.jumkid.media.repository.FileSearchRepository;
import com.jumkid.media.repository.FileStorage;
import com.jumkid.media.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.jumkid.media.exception.MediaStoreServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("fileService")
public class MediaFileServiceImpl extends AbstractCommonService implements MediaFileService {

	public static final Logger logger = LoggerFactory.getLogger(MediaFileServiceImpl.class);

	@Autowired
	private FileStorage<MediaFile> fileStorage;

	@Autowired
	private FileSearchRepository fileSearch;

    @Override
    public MediaFile saveMediaFile(MediaFile mfile, byte[] file) throws MediaStoreServiceException {
        logger.debug("save media file");

        if (mfile.getId()==null) {
            mfile.setModule(Constants.MODULE_FILE);
            mfile.setCreatedDate(new Date());
            mfile.setActivated(true);
        }

        if(mfile.getFilename()==null){
            mfile.setFilename(mfile.getId());
        }

        return saveFile(file, mfile);
    }

	@Override
	public MediaFile getMediaFile(String id) throws MediaStoreServiceException {
    	logger.debug("Retrieve media file by given id ", id);

		Optional<MediaFile> opt = fileSearch.findById(id);
		if (!opt.isPresent()) throw new MediaStoreServiceException("No media file is found by id");
		return opt.get();
	}

    @Override
    public FileChannel getSourceFile(String id) throws MediaStoreServiceException {
        logger.debug("Retrieve source file by given id ", id);

        Optional<MediaFile> opt = fileSearch.findById(id);
        if (!opt.isPresent()) throw new MediaStoreServiceException("No media file is found by id");
        MediaFile mfile = opt.get();
        Boolean isRandomAccess = (mfile.getMimeType().startsWith("video") ||
                mfile.getMimeType().startsWith("audio"))
                ? true : false;
        FileChannel fc = isRandomAccess ? fileStorage.getRandomAccessFile(mfile) : fileStorage.getFile(mfile);

        return fc;
    }

    @Override
	public Page<MediaFile> searchFile(String keyword, Integer start, Integer limit) throws MediaStoreServiceException {
        logger.debug("Search file by keyword [", keyword, "]");

        Page<MediaFile> page = null;
        Pageable pager = PageRequest.of(start, limit);

        if(keyword!=null && !keyword.isEmpty()){
            page = fileSearch.findByFilenameContainingAndModule(keyword, Constants.MODULE_FILE, pager);
        }

        return page;
    }

	@Override
	public Page<MediaFile> getAllFiles(Integer start, Integer limit) throws MediaStoreServiceException {
		logger.debug("Get all files");

		Page<MediaFile> page = null;
		Pageable pager = PageRequest.of(start, limit);
		page = fileSearch.findByModule(Constants.MODULE_FILE, pager);

		return page;
	}

	/**
	 * 
	 */
	public synchronized ServiceCommand execute(ServiceCommand cmd) throws MediaStoreServiceException {
		
		try{
			if (isAction(cmd,"retrieve")) {
				String uuid = (String)cmd.getParams().get("uuid");
				String filename = (String)cmd.getParams().get("filename");

				MediaFile mfile = null;
				if(uuid!=null){
                    Optional<MediaFile> opt = fileSearch.findById(uuid);
                    mfile = opt.get();
				}else
				if(filename!=null){
					mfile = fileSearch.findByFilenameAndModule(filename, Constants.MODULE_FILE);
				}
				if(mfile!=null){
					Boolean isRandomAccess = (mfile.getMimeType().startsWith("video") ||
							mfile.getMimeType().startsWith("audio"))
							? true : false;
					FileChannel fc = isRandomAccess ? fileStorage.getRandomAccessFile(mfile) : fileStorage.getFile(mfile);

					cmd.getResults().put("fileChannel", fc);

					cmd.getResults().put("mfile", mfile);
				}else{
					cmd.addError("Failed to retrieve file information", mfile);
				}

			}else
			// get file thumbnail
			if (isAction(cmd, "thumbnail")) {
				String uuid = (String)cmd.getParams().get("uuid");
				boolean large = (boolean)cmd.getParams().get("large");
				String scope = (String)cmd.getParams().get("scope");

				if(uuid!=null){
                    Optional<MediaFile> opt = fileSearch.findById(uuid);
					MediaFile mfile = opt.get();
					if(mfile!=null){
						FileChannel fc = fileStorage.getThumbnail(mfile, large);

						cmd.getResults().put("fileChannel", fc);

						cmd.getResults().put("mfile", mfile);
					}else{
						cmd.addError("Failed to retrieve file information", mfile);
					}

				}

			}else
			// get file thumbnail
			if (isAction(cmd,"avatar")) {
				String uuid = (String)cmd.getParams().get("uuid");
				boolean large = (boolean)cmd.getParams().get("large");

				MediaFile mfile = null;
				if(uuid!=null && !uuid.isEmpty()){
                    Optional<MediaFile> opt = fileSearch.findById(uuid);
					mfile = opt.get();
				}

				if(mfile==null){
					mfile = new MediaFile();
					mfile.setMimeType("avatar");
				}

				FileChannel fc = fileStorage.getThumbnail(mfile, large);

				cmd.getResults().put("fileChannel", fc);
				cmd.getResults().put("mfile", mfile);

			}else
			// delete file
			if (isAction(cmd, "delete")) {
				String uuid = (String)cmd.getParams().get("uuid");

				if (uuid!=null) {
                    Optional<MediaFile> opt = fileSearch.findById(uuid);
					MediaFile mfile = opt.get();
					if (mfile!=null) {
						//delete file from storage
						fileStorage.deleteFile(mfile);
						//remove from index
						fileSearch.delete(mfile);

					}else{
						cmd.addError("Failed to retrieve file information", mfile);
					}

				}

			}
        	
        } catch (Exception e) {
        	logger.error("failed to perform ", cmd.getAction(), " due to ", e.getMessage());
            cmd.addError(e.getLocalizedMessage(), null);
        }
        
        return cmd;
	}

    @Transactional
    MediaFile saveFile(byte[] file, MediaFile mfile) throws MediaStoreServiceException{
        //TODO: extract meta data and content from office document

        if(file!=null){
            mfile = fileStorage.saveFile(file, mfile);
        }

        //index media file for search
        if(mfile!=null && mfile.getLogicalPath()!=null){
            if( fileSearch.save(mfile) == null )
                throw new MediaStoreServiceException("Failed to index media file");
        }

	    return mfile;
    }
	
	@Override
	public MediaFile transformRequestToMediaFile(MultipartFile file, HttpServletRequest request){

	    logger.debug("tranform multipart file request");

		String uuid = request.getParameter("uuid");
		MediaFile mfile;
		if (uuid!=null && !uuid.isEmpty()) {
            Optional<MediaFile> opt = fileSearch.findById(uuid);
			mfile = opt.get();
        } else {
        	mfile = new MediaFile();
        	//set file name
            logger.debug("file name: ", file.getName());
            logger.debug("file original name: ", file.getOriginalFilename());
        	mfile.setFilename(file.getOriginalFilename());
        	//set file size
            logger.debug("file size: ", file.getSize());
        	mfile.setSize(file.getSize());
        	//set file type
            logger.debug("file type: ", file.getContentType());
            mfile.setMimeType(file.getContentType());
            //set title
            mfile.setTitle(request.getParameter("title"));
        }    
		
		//parse request by fields
		//mfile = (MediaFile)this.fillInValueByRequest(mfile, request);
        
		//mfile = (MediaFile)this.fillInConcurrencyInfo(mfile, request);

		return mfile;
	}
	
}
