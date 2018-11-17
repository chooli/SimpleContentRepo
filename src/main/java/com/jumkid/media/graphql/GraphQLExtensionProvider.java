package com.jumkid.media.graphql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("graphqlExtProvider")
public class GraphQLExtensionProvider{

    public static final Logger logger = LoggerFactory.getLogger(GraphQLExtensionProvider.class);

    /**
     * Register graphql resolver and use them in schema generation
     *
     * @return
     */
    public Collection<Object> getExtensions() {
        List<Object> extensions = new ArrayList<>();

        extensions.add(new com.jumkid.media.graphql.mfile.QueryResolver());
        logRegistryInfo("mfile query");

        return extensions;
    }

    private void logRegistryInfo(String name){
        logger.info("[+]GraphQL initiate > add resolver: ", name);
    }

}