package com.jumkid.media.controller;

import com.google.common.collect.ImmutableList;
import com.jumkid.media.model.MediaFile;
import com.jumkid.media.service.MediaFileService;
import com.jumkid.media.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created at Oct2018$
 *
 * @author chooliyip
 **/
@RestController
@RequestMapping("/content")
public class MediaContentController {

    @Autowired
    private MediaFileService fileService;

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
            response.setErrors(ImmutableList.of("media file is not found"));
        }

        return response.getErrors().toString();
    }

    public void setFileService(MediaFileService fileService) {
        this.fileService = fileService;
    }

}
