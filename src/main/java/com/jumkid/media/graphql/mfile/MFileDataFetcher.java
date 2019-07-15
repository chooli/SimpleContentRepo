package com.jumkid.media.graphql.mfile;
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
import com.jumkid.media.exception.MediaStoreServiceException;
import com.jumkid.media.model.MediaFile;
import com.jumkid.media.service.MediaFileService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MFileDataFetcher {

    private MediaFileService fileService;

    @Autowired
    public MFileDataFetcher(MediaFileService fileService) {
        this.fileService = fileService;
    }

    public DataFetcher getMFile(){
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument(MediaFile.Fields.ID.value());
            try {
                return fileService.getMediaFile(id);
            } catch (MediaStoreServiceException e) {
                return e.getMessage();
            }

        };
    }

    public DataFetcher allMFile() {
        return dataFetchingEnvironment -> {
            try {
                return fileService.getAll();
            } catch (MediaStoreServiceException e) {
                return e.getMessage();
            }
        };
    }

}
