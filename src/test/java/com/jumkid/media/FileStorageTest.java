package com.jumkid.media;

import com.jumkid.media.service.MediaFileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FileStorageTest {


    @Configuration
    static class ContextConfiguration {
        //void
    }

    @Before
    public void setUp(){

    }

    @Test
    public void testUpdate(){
        assertTrue(true);
    }


}
