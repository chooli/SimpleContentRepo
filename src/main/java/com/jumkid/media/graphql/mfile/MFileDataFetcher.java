package com.jumkid.media.graphql.mfile;

import com.jumkid.media.exception.MediaStoreServiceException;
import com.jumkid.media.model.MediaFile;
import com.jumkid.media.service.MediaFileService;
import com.jumkid.media.util.Constants;
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

    public DataFetcher getMFileDataFetcher(){
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument(MediaFile.Fields.ID.value());
            try {
                return fileService.getMediaFile(id);
            } catch (MediaStoreServiceException e) {
                return e.getMessage();
            }

        };
    }

}
