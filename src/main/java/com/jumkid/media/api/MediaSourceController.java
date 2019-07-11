package com.jumkid.media.api;
/*
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 *
 */

import com.jumkid.media.model.MediaFile;
import com.jumkid.media.service.MediaFileService;
import com.jumkid.media.util.Response;
import com.jumkid.media.util.ResponseMediaFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created at 18 Sep, 2018$
 *
 * @author chooliyip
 **/
@RestController
@RequestMapping("/api")
public class MediaSourceController {

    private static final Logger logger = LoggerFactory.getLogger(MediaSourceController.class);

    private MediaFileService fileService;

    private ResponseMediaFileWriter responseMediaFileWriter;

    @Autowired
    public MediaSourceController(MediaFileService fileService, ResponseMediaFileWriter responseMediaFileWriter) {
        this.fileService = fileService;
        this.responseMediaFileWriter = responseMediaFileWriter;
    }

    @PostMapping("/mfile/upload")
    @ResponseBody
    public Response saveMedia(@RequestParam("file") MultipartFile file){
        Response response = new Response();

        try {
            MediaFile mfile = new MediaFile.Builder()
                    .title(file.getName())
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

    @GetMapping("/mfile/download/{id}")
    @ResponseBody
    public Response getMedia(@PathVariable("id") String id, HttpServletResponse httpResponse){
        Response response = new Response();

        Optional opt = fileService.getSourceFile(id);
        try {
            if(opt.isPresent()) {
                MediaFile mfile = fileService.getMediaFile(id);
                byte[] bytes = (byte[])opt.get();
                responseMediaFileWriter.writeForDownload(mfile, bytes, httpResponse);
            }
        } catch (IOException ioe) {
            response.setSuccess(false);
            response.addError("Fail to get upload file from request");
        }

        return response;
    }

}
