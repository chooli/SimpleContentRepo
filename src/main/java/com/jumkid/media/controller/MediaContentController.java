package com.jumkid.media.controller;

import com.jumkid.media.exception.MediaStoreServiceException;
import com.jumkid.media.model.MediaFile;
import com.jumkid.media.service.MediaFileService;
import com.jumkid.media.util.Response;
import com.jumkid.media.util.ResponseMediaFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Created at Oct2018$
 *
 * @author chooliyip
 **/
@RestController
@RequestMapping("/content")
public class MediaContentController {

    public static final Logger logger = LoggerFactory.getLogger(MediaContentController.class);

    @Autowired
    private MediaFileService fileService;

    @Autowired
    private ResponseMediaFileWriter responseMFileWriter;

    @GetMapping("/plain/{id}")
    @ResponseBody
    public String getContent(@PathVariable("id") String id){
        //TODO validate id

        Response response = new Response();
        MediaFile mfile = fileService.getMediaFile(id);
        if(mfile!=null){
            return mfile.getContent();
        }else{
            response.setSuccess(false);
            response.addError("media file is not found");
        }

        return response.getErrors().toString();
    }

    @GetMapping("/html/{id}")
    public ResponseEntity<String> getHtmlContent(@PathVariable("id") String id){
        //TODO validate id

        Response response = new Response();
        HttpHeaders responseHeaders = new HttpHeaders();
        FileChannel fc = null;
        try{
            fc = fileService.getSourceFile(id);
            ByteBuffer buf = ByteBuffer.allocate(1024);
            StringBuffer sb = new StringBuffer();
            while (fc.read(buf) != -1) {
                buf.flip();
                sb.append(Charset.defaultCharset().decode(buf));
                buf.clear();
            }

            responseHeaders.setContentType(MediaType.TEXT_HTML);
            return new ResponseEntity<String>(sb.toString(), responseHeaders, HttpStatus.FOUND);
        }catch (MediaStoreServiceException msse) {
            response.setSuccess(false);
            response.addError("media file is not found");
            responseHeaders.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<String>(response.getErrors().toString(), responseHeaders, HttpStatus.NOT_FOUND);
        }catch (IOException ioe) {
            response.setSuccess(false);
            response.addError("failed to read media file content");
            responseHeaders.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<String>(response.getErrors().toString(), responseHeaders, HttpStatus.EXPECTATION_FAILED);
        }finally {
            try{
                if(fc!=null) fc.close();
            }catch (IOException e){
                e.printStackTrace();
                logger.error("failed to handle file resource ", e.getMessage());
            }

        }

    }

    @GetMapping(value="/stream/{id}")
    public void stream(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response){
        //TODO valid id
        FileChannel fileChannel = null;
        try {
            MediaFile mfile = fileService.getMediaFile(id);
            fileChannel = fileService.getSourceFile(id);
            if(mfile.getMimeType().startsWith("audio") || mfile.getMimeType().startsWith("video")){
                response = responseMFileWriter.stream(mfile, fileChannel, request, response);
            }else{
                response = responseMFileWriter.write(mfile, fileChannel, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try{
                //if(fileChannel!=null) fileChannel.close();
                response.flushBuffer();
            }catch(Exception e){
                e.printStackTrace();
                logger.error("failed to get file resource ", e.getMessage());
            }
        }

    }

    public void setFileService(MediaFileService fileService) {
        this.fileService = fileService;
    }

    public void setResponseMFileWriter(ResponseMediaFileWriter responseMFileWriter) {
        this.responseMFileWriter = responseMFileWriter;
    }

}
