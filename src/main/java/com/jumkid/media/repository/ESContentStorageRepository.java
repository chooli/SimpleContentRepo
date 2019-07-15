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
 *
 */

import com.jumkid.media.exception.MediaStoreServiceException;
import com.jumkid.media.model.MediaFile;
import static com.jumkid.media.util.Constants.*;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

import static com.jumkid.media.model.MediaFile.Fields.*;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Repository("esContentStorage")
public class ESContentStorageRepository implements FileSearch<MediaFile> {

    private static final Logger logger = LoggerFactory.getLogger(ESContentStorageRepository.class);

    private final RestHighLevelClient esClient;

    @Autowired
    public ESContentStorageRepository(RestHighLevelClient esClient) {
        this.esClient = esClient;
    }

    @Override
    public MediaFile saveMetadata(MediaFile mfile) {
        return saveMetadata(null, mfile);
    }

    @Override
    public MediaFile saveMetadata(byte[] bytes, MediaFile mfile) {
        try {
            XContentBuilder builder = XContentFactory.cborBuilder();
            builder.startObject()
                    .field(TITLE.value(), mfile.getTitle())
                    .field(FILENAME.value(), mfile.getFilename())
                    .field(SIZE.value(), mfile.getSize())
                    .field(MODULE.value(), mfile.getModule())
                    .field(MIMETYPE.value(), mfile.getMimeType())
                    .field(CONTENT.value(), mfile.getContent())
                    .field(ACTIVATED.value(), mfile.isActivated())
                    .timeField(CREATED_DATE.value(), mfile.getCreatedDate())
                    .field(CREATED_BY.value(), mfile.getCreatedBy())
                    .field(BLOB.value(), bytes)
                    .endObject();
            IndexRequest request = new IndexRequest(MODULE_MFILE).source(builder);
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
//            request.opType(DocWriteRequest.OpType.CREATE);
//            request.setPipeline("pipeline");

            //Synchronous execution
            IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
            mfile.setId(response.getId());

            return mfile;
        } catch (IOException ioe) {
            logger.error("failed to create index {} ", ioe.getMessage());
            throw new MediaStoreServiceException("Not able to save media file into Elasticsearch, please contact system administrator.");
        }
    }

    @Override
    public MediaFile getMetadata(String id) {

        GetRequest request = new GetRequest(MODULE_MFILE).id(id);

        try {
            GetResponse getResponse = esClient.get(request, RequestOptions.DEFAULT);
            if(!getResponse.isExists()) {
                return null;
            }
            return new MediaFile.Builder()
                    .id(getResponse.getId())
                    .createdBy((String)getResponse.getSource().get(CREATED_BY.value()))
                    //.createdDate((Date)getResponse.getSource().get(CREATED_DATE.value()))
                    .mimeType((String)getResponse.getSource().get(MIMETYPE.value()))
                    .module((String)getResponse.getSource().get(MODULE.value()))
                    .content((String)getResponse.getSource().get(CONTENT.value()))
                    .activated((Boolean)getResponse.getSource().get(ACTIVATED.value()))
                    .filename((String)getResponse.getSource().get(FILENAME.value()))
                    .title((String)getResponse.getSource().get(TITLE.value()))
                    .size((Integer)getResponse.getSource().get(SIZE.value()))
                    .logicalPath((String)getResponse.getSource().get(LOGICALPATH.value()))
                    .build();
        } catch (IOException ioe) {
            logger.error("failed to get media file {} ", ioe.getMessage());
            throw new MediaStoreServiceException("Not able to get media file from Elasticsearch, please contact system administrator.");
        }

    }

    @Override
    public Optional<byte[]> getBinary(String id) {
        GetRequest request = new GetRequest(MODULE_MFILE).id(id);
        try {
            GetResponse getResponse = esClient.get(request, RequestOptions.DEFAULT);

            //TODO investigate why ES returns base64 encoded field value
            byte[] bytes = Base64.getDecoder().decode((String)getResponse.getSource().get(BLOB.value()));
            return Optional.of(bytes);
        } catch (IOException ioe) {
            logger.error("failed to get source file {} ", ioe.getMessage());
            throw new MediaStoreServiceException("Not able to get media file from Elasticsearch, please contact system administrator.");
        }
    }

    @Override
    public boolean deleteMetadata(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(MODULE_MFILE).id(id);
        deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        try {
            DeleteResponse deleteResponse = esClient.delete(deleteRequest, RequestOptions.DEFAULT);
            return deleteResponse.getResult() == DocWriteResponse.Result.DELETED;
        } catch (IOException ioe) {
            logger.error("failed to delete media file {} ", ioe.getMessage());
            throw new MediaStoreServiceException("Not able to delete media file id[" + id + "] from Elasticsearch, please contact system administrator.");
        }
    }

    @Override
    public MediaFile updateMetadata(MediaFile mediaFile) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(MODULE_MFILE);
        updateRequest.id(mediaFile.getId());

        try {

            updateRequest.doc(jsonBuilder()
                    .startObject()
                    .field(TITLE.value(), mediaFile.getTitle())
                    .field(FILENAME.value(), mediaFile.getFilename())
                    .field(CONTENT.value(), mediaFile.getContent())
                    .field(MIMETYPE.value(), mediaFile.getMimeType())
                    .field(SIZE.value(), mediaFile.getSize())
                    .field(LOGICALPATH.value(), mediaFile.getLogicalPath())
                    .endObject());

            esClient.update(updateRequest, RequestOptions.DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("failed to update metadata {}", e.getMessage());
        }

        return mediaFile;
    }

    @Override
    public List<MediaFile> getAll() {
        List<MediaFile> results = new ArrayList<>();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.searchType(SearchType.DEFAULT);

        try {
            SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
            response.getHits().iterator().forEachRemaining( hitDoc -> {
                Map<String, Object> sourceMap = hitDoc.getSourceAsMap();
                MediaFile mfile = new MediaFile.Builder()
                        .id(hitDoc.getId())
                        .title(sourceMap.get(TITLE.value()) !=null ? sourceMap.get(TITLE.value()).toString() : null)
                        .filename(sourceMap.get(FILENAME.value()) !=null ? sourceMap.get(FILENAME.value()).toString() : null)
                        .mimeType(sourceMap.get(MIMETYPE.value()) !=null ? sourceMap.get(MIMETYPE.value()).toString() : null)
                        .size(sourceMap.get(SIZE.value()) != null ? (Integer)sourceMap.get(SIZE.value()) : null)
                        .createdBy(sourceMap.get(CREATED_BY.value()) != null ? sourceMap.get(CREATED_BY.value()).toString() : null)
                        .activated(sourceMap.get(ACTIVATED.value()) != null ? (Boolean) sourceMap.get(ACTIVATED.value()) : Boolean.FALSE)
                        .content(sourceMap.get(CONTENT.value()) !=null ? sourceMap.get(CONTENT.value()).toString() : null)
                        .build();
                results.add(mfile);
            });

        } catch (IOException ioe) {
            logger.error("failed to search media files {} ", ioe.getMessage());
            throw new MediaStoreServiceException("Not able to fetch all media files from Elasticsearch, please contact system administrator.");
        }

        return results;
    }

}
