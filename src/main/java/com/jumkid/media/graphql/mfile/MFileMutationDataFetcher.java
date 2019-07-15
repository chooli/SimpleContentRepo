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
import static com.jumkid.media.model.MediaFile.Fields.*;
import com.jumkid.media.service.MediaFileService;

import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MFileMutationDataFetcher {

    private MediaFileService fileService;

    @Autowired
    public MFileMutationDataFetcher(MediaFileService fileService) {
        this.fileService = fileService;
    }

    public DataFetcher createMFileMutation() {
        return dataFetchingEnvironment -> {
            MediaFile mfile = new MediaFile.Builder()
                    .title(dataFetchingEnvironment.getArgument(TITLE.value()))
                    .filename(dataFetchingEnvironment.getArgument(FILENAME.value()))
                    .activated(dataFetchingEnvironment.getArgument(ACTIVATED.value()))
                    .content(dataFetchingEnvironment.getArgument(CONTENT.value()))
                    .createdBy(dataFetchingEnvironment.getArgument(CREATED_BY.value()))
                    .createdDate(dataFetchingEnvironment.getArgument(CREATED_DATE.value()))
                    .mimeType(dataFetchingEnvironment.getArgument(MIMETYPE.value()))
                    .size(dataFetchingEnvironment.getArgument(SIZE.value()))
                    .module(dataFetchingEnvironment.getArgument(MODULE.value()))
                    .build();

            byte[] bytes = null;

            return fileService.saveMediaFile(mfile, bytes);
        };
    }

    public DataFetcher deleteMFileMutation() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument(MediaFile.Fields.ID.value());
            try {
                return fileService.deleteMediaFile(id);
            } catch (MediaStoreServiceException e) {
                return e.getMessage();
            }
        };
    }

}
