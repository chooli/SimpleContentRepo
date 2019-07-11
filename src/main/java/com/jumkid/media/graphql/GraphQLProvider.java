/*
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 *
 * (c)2019 Jumkid All rights reserved.
 *
 */
package com.jumkid.media.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.jumkid.media.graphql.mfile.MFileDataFetcher;
import com.jumkid.media.graphql.mfile.MFileMutationDataFetcher;
import com.jumkid.media.graphql.mfile.SimpleMFileTestFetchers;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    private GraphQLSchema graphQLSchema;

    private GraphQL graphQL;

    @Value("${graphql.schema.file}")
    private String schemaFile;

    private SimpleMFileTestFetchers simpleMFileTestFetchers;

    private MFileDataFetcher mFileDataFetcher;

    private MFileMutationDataFetcher mFileMutationDataFetcher;

    @Autowired
    public GraphQLProvider(SimpleMFileTestFetchers simpleMFileTestFetchers, MFileDataFetcher mFileDataFetcher,
                           MFileMutationDataFetcher mFileMutationDataFetcher) {
        this.simpleMFileTestFetchers = simpleMFileTestFetchers;
        this.mFileDataFetcher = mFileDataFetcher;
        this.mFileMutationDataFetcher = mFileMutationDataFetcher;
    }

    @Bean
    public GraphQL graphQL() {
        if (graphQL == null) {
            graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        }
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource(schemaFile);
        String sdl = Resources.toString(url, Charsets.UTF_8);
        this.graphQLSchema = buildSchema(sdl);
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("allMFile", simpleMFileTestFetchers.getAllMFile())
                        .dataFetcher("mfileById", mFileDataFetcher.getMFileDataFetcher())
                )
                .type(newTypeWiring("Mutation")
                        .dataFetcher("createMFile", mFileMutationDataFetcher.createMFileMutationDataFetcher())
                        .dataFetcher("deleteMFile", mFileMutationDataFetcher.deleteMFileMutationDataFetcher())
                )
                .build();
    }

    public GraphQLSchema getGraphQLSchema() {
        return graphQLSchema;
    }

}
