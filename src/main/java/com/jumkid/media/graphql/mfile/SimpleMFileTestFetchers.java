package com.jumkid.media.graphql.mfile;

import com.google.common.collect.ImmutableList;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Map.of;

@Component
public class SimpleMFileTestFetchers {

    private static final List<Map> mfiles = ImmutableList.of(
            of("id", "1", "filename", "test-1", "title", "Test 1", "size", 10000, "mimeType", "text/plain", "createdDate", new Date().getTime(), "createdBy", "system", "content", "this is text content"),
            of("id", "2", "filename", "test-2", "title", "Test 2", "createdDate", new Date().getTime(), "createdBy", "system", "content", "this is text content"),
            of("id", "3", "filename", "test-3", "title", "Test 3", "createdDate", new Date().getTime(), "createdBy", "system", "content", "this is text content")
    );

    public DataFetcher getMFileByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            return mfiles
                    .stream()
                    .filter(mfile -> mfile.get("id").equals(id))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getAllMFile() {
        return dataFetchingEnvironment -> mfiles;
    }

}
