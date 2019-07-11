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
package com.jumkid.media;

import com.jumkid.media.graphql.GraphQLProvider;
import graphql.servlet.SimpleGraphQLHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


/**
 * Main application endpoint with spring boot
 *
 * Created at Sep2018$
 *
 * @author chooliyip
 **/

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private GraphQLProvider graphQLProvider;

    @Autowired
    public Application(GraphQLProvider graphQLProvider){
        this.graphQLProvider = graphQLProvider;
    }

    @Override
    public void run(String... args) {
        logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.",
                Arrays.toString(args));
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean<>(SimpleGraphQLHttpServlet.newBuilder(graphQLProvider.getGraphQLSchema()).build(), "/graphql");
    }

}
