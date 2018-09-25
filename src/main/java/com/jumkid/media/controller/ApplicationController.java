package com.jumkid.media.controller;

import com.jumkid.media.Application;
import com.jumkid.media.model.AppInfo;
import com.jumkid.media.util.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * This is a restful api for application info
 *
 * Created at Sep2018$
 *
 * @author chooliyip
 **/
@RestController
@RequestMapping("/")
public class ApplicationController {

    @Value("${spring.application.name}")
    String appName;

    @GetMapping
    @ResponseBody
    public Response appInfo(){
        AppInfo appInfo = new AppInfo();
        //set info
        appInfo.setName(appName);
        appInfo.setVersion(Application.class.getPackage().getImplementationVersion());
        appInfo.setDescription("Media server provides media streaming resources and services");

        Response response = this.buildResponse(true, appInfo);

        return response;
    }

    /**
     *
     * @param success
     * @param data
     * @return
     */
    private Response buildResponse(boolean success, Object data){
        final Response response = new Response();
        response.setSuccess(success);
        response.setData(data);

        return response;
    }

}
