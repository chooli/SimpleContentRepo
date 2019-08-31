package com.jumkid.media.controller;

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


import com.jumkid.media.model.AppInfo;
import com.jumkid.media.util.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * This is a restful controller for application info
 *
 * Created at Sep2018$
 *
 * @author chooliyip
 **/
@RestController
@RequestMapping("/status")
public class StatusCheckController {

    @Value("${spring.application.name}")
    String appName;

    @Value("${spring.application.version}")
    String appVersion;

    @Value("${spring.application.api}")
    String appApi;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public Response appInfo(){
        AppInfo appInfo = new AppInfo();
        //set info
        appInfo.setName(appName);
        appInfo.setVersion(appVersion);
        appInfo.setDescription("Media server provides media streaming resources and services");
        appInfo.setApi(appApi);

        return this.buildResponse(true, appInfo);

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
