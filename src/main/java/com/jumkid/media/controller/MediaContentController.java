package com.jumkid.media.controller;

/*
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 *
 * (c)2019 Jumkid Innovation All rights reserved.
 */

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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Optional;

@RestController
@RequestMapping("/content")
public class MediaContentController {

    private static final Logger logger = LoggerFactory.getLogger(MediaContentController.class);

    private MediaFileService fileService;

    private ResponseMediaFileWriter responseMFileWriter;

    @Autowired
    public MediaContentController(MediaFileService fileService, ResponseMediaFileWriter responseMFileWriter) {
        this.fileService = fileService;
        this.responseMFileWriter = responseMFileWriter;
    }

    @GetMapping("/plain/{id}")
    @ResponseBody
    public String getContent(@PathVariable("id") String id){
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
    @ResponseBody
    public ResponseEntity<String> getHtmlContent(@PathVariable("id") String id){
        //TODO validate id

        Response response = new Response();
        HttpHeaders responseHeaders = new HttpHeaders();
        FileChannel fc = null;
        try{
            Optional<FileChannel> opt = fileService.getSourceFile(id);
            if (!opt.isPresent()) return null;

            fc = opt.get();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            StringBuilder sb = new StringBuilder();
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
                logger.error("failed to handle file resource {}", e.getMessage());
            }

        }

    }

    @PostMapping("/upload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file, HttpServletRequest httpRequest){
        Response response = new Response();

        try {
            String title = httpRequest.getParameter("title");
            MediaFile mfile = new MediaFile.Builder()
                    .title(title != null ? title : file.getName())
                    .filename(file.getOriginalFilename())
                    .size((int)file.getSize())
                    .mimeType(file.getContentType())
                    .build();

            response.setData(fileService.saveMediaFile(mfile, file.getBytes()));
            response.setTotal(1L);

            logger.debug("media file {} uploaded", mfile.getFilename());

        } catch (IOException ioe) {
            response.setSuccess(false);
            response.addError("Fail to get upload file from request");
        }

        return response;
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public Response download(@PathVariable("id") String id, HttpServletResponse httpResponse){
        Response response = new Response();

        Optional opt = fileService.getSourceFile(id);
        try {
            if(opt.isPresent()) {
                MediaFile mfile = fileService.getMediaFile(id);
                byte[] bytes = (byte[])opt.get();
                responseMFileWriter.writeForDownload(mfile, bytes, httpResponse);
            }
        } catch (IOException ioe) {
            response.setSuccess(false);
            response.addError("Fail to get upload file from request");
        }

        return response;
    }

    @GetMapping(value="/stream/{id}")
    public void stream(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response){
        //TODO valid id
        FileChannel fileChannel = null;
        try {
            MediaFile mfile = fileService.getMediaFile(id);

            Optional<FileChannel> opt = fileService.getSourceFile(id);
            if(!opt.isPresent()) {
                response = responseMFileWriter.write(mfile, null, response);
            } else if(mfile.getMimeType().startsWith("audio") || mfile.getMimeType().startsWith("video")){
                response = responseMFileWriter.stream(mfile, fileChannel, request, response);
            } else {
                response = responseMFileWriter.write(mfile, fileChannel, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                response.flushBuffer();
            }catch(Exception e){
                e.printStackTrace();
                logger.error("failed to get file resource {}", e.getMessage());
            }
        }

    }

}
