package com.jumkid.media.controller;

import com.jumkid.media.exception.MediaStoreServiceException;
import com.jumkid.media.model.MediaFile;
import com.jumkid.media.service.MediaFileService;
import com.jumkid.media.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created at 18 Sep, 2018$
 *
 * @author chooliyip
 **/
@RestController
@RequestMapping("/media")
public class MediaSourceConstroller {

    public static final Logger logger = LoggerFactory.getLogger(MediaSourceConstroller.class);

    @Autowired
    private MediaFileService fileService;

    @PostMapping("/upload")
    @ResponseBody
    public Response saveMedia(@RequestParam("file") MultipartFile file,
                              HttpServletRequest request){
        Response response = new Response();

        MediaFile mfile = fileService.transformRequestToMediaFile(file, request);
        try {
            byte[] _file = file.getBytes();

            mfile = fileService.saveMediaFile(mfile, _file);

            response.setData(mfile);
            response.setTotal(1L);
        } catch (IOException ioe) {
            response.setSuccess(false);
            response.addError("Fail to get upload file from request");
        }

        return response;
    }

    @PutMapping("save")
    @ResponseBody
    public Response saveContent(@RequestParam("title") String title, @RequestParam("author") String author,
                                @RequestParam("content") String content){
        Response response = new Response();
        //create mfile object
        MediaFile mfile = new MediaFile();
        mfile.setTitle(title);
        mfile.setCreatedBy(author);
        mfile.setContent(content);
        mfile.setMimeType("text/html");
        try{
            mfile = fileService.saveMediaFile(mfile, null);
            response.setData(mfile);
        } catch (MediaStoreServiceException e) {
            response.addError("Failed to save content");
        }

        return response;
    }

    @GetMapping("/info/{id}")
    @ResponseBody
    public Response getMedia(@PathVariable("id") String id){
        Response response = new Response();
        response.setData(fileService.getMediaFile(id));
        response.setTotal(1L);
        return response;
    }

    @PostMapping("/search")
    @ResponseBody
    public Response searchMedia(@RequestParam("keyword") String keyword, @RequestParam("start") Integer start,
                                    @RequestParam("limit") Integer limit){

        Response response = new Response();

        Page<MediaFile> page = fileService.searchFile(keyword, start, limit);
        response.setData(page.getContent());
        response.setTotal(page.getTotalElements());

        return response;
    }

    @GetMapping("/all")
    @ResponseBody
    public Response getAllMedia(@RequestParam("start") Integer start,
                                @RequestParam("limit") Integer limit){
        Response response = new Response();

        Integer _start = (start == null ? 0 : start);
        Integer _limit = (limit == null ? 20 : limit);

        Page<MediaFile> page = fileService.getAllFiles(_start, _limit);
        response.setData(page.getContent());
        response.setTotal(page.getTotalElements());

        return response;
    }

}
