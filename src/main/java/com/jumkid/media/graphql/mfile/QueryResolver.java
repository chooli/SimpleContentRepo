package com.jumkid.media.graphql.mfile;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.jumkid.media.model.MediaFile;
import com.jumkid.media.service.MediaFileService;
import com.jumkid.media.util.ResponseMediaFileWriter;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created at Nov2018$
 *
 * @author chooliyip
 **/
public class QueryResolver{

    public static final Logger logger = LoggerFactory.getLogger(QueryResolver.class);

    @Autowired
    private MediaFileService fileService;

    @Autowired
    private ResponseMediaFileWriter responseMFileWriter;

    @GraphQLQuery
    public MediaFile mfileById(String id){
        logger.debug("get mfile by id ", id);

        MediaFile mfile = new MediaFile();
        mfile.setId("XXXXXXXXXXXXXXX");
        mfile.setTitle("chooli test here");

        return mfile;
    }

}
