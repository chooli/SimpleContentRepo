package com.jumkid.media;

import com.jumkid.media.graphql.GraphQLExtensionProvider;
import com.jumkid.media.graphql.mfile.QueryResolver;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.ExecutionStrategy;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLHttpServlet;
import io.leangen.graphql.GraphQLSchemaGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

import static graphql.execution.ExecutionId.generate;

/**
 * Main application endpoint with spring boot
 *
 * Created at Sep2018$
 *
 * @author chooliyip
 **/
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private GraphQLExtensionProvider graphqlExtProvider;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.",
                Arrays.toString(args));
    }

    @Bean
    GraphQLSchema schema() {

        return new GraphQLSchemaGenerator()
                .withOperationsFromSingletons(graphqlExtProvider.getExtensions().toArray())
                .generate();
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ExecutionStrategy executionStrategy = new AsyncExecutionStrategy();
        return new ServletRegistrationBean(SimpleGraphQLHttpServlet.newBuilder(schema()).build(), "/graphql");
    }

    public void setGraphqlExtProvider(GraphQLExtensionProvider graphqlExtProvider) {
        this.graphqlExtProvider = graphqlExtProvider;
    }
}
