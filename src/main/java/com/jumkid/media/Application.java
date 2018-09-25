package com.jumkid.media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

    public static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.",
                Arrays.toString(args));
    }
}
