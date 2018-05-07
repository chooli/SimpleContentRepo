package com.jahia.poc.controller;

/**
 * controller class for answer api
 */

import com.jahia.poc.model.Answer;
import com.jahia.poc.service.AnswerService;
import com.jahia.poc.service.ServiceCommand;
import com.jahia.poc.util.Constants;
import com.jahia.poc.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/answer")
@RestController
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @RequestMapping(method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response newAnswer(@RequestBody Answer answer){
        //TODO: validate the answer object before save it
        if(answer.getId()!=null && answer.getId().isEmpty()) answer.setId(null);

        //construct service command
        ServiceCommand cmd = buildCommand(Constants.SERVICE_ACTION_SAVE);
        cmd.addParam("answer", answer);

        cmd = answerService.execute(cmd);

        //TODO: replace by a better response object
        return buildResponse(true, (Answer)cmd.getResult("answer"));
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Response updateAnswer(@RequestBody Answer answer){
        //TODO: validate the answer object before save it
        if(answer.getId()!=null && answer.getId().isEmpty()) answer.setId(null);

        //construct service command
        ServiceCommand cmd = buildCommand(Constants.SERVICE_ACTION_UPDATE);
        cmd.addParam("answer", answer);
        cmd = answerService.execute(cmd);

        //TODO: replace by a better response object
        return buildResponse(true, (Answer)cmd.getResult("answer"));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Response getAnswer(@PathVariable("id") String answerId){
        //construct service command
        ServiceCommand cmd = buildCommand(Constants.SERVICE_ACTION_FIND);
        cmd.addParam("id", answerId);

        cmd = answerService.execute(cmd);

        //TODO: replace by a better response object
        return buildResponse(true, (Answer)cmd.getResult("answer"));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Response getAnswers(){
        //construct service command
        ServiceCommand cmd = buildCommand(Constants.SERVICE_ACTION_LIST);

        cmd = answerService.execute(cmd);

        return buildResponse(true, (List<Answer>)cmd.getResult("answers"));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Response deleteAnswer(@PathVariable("id") String answerId){
        //construct service command
        ServiceCommand cmd = buildCommand(Constants.SERVICE_ACTION_DELETE);
        cmd.addParam("id", answerId);

        cmd = answerService.execute(cmd);

        //TODO: replace by a better response object
        return buildResponse(true, null);
    }

    /**
     *
     * @param action
     * @return
     */
    private ServiceCommand buildCommand(String action){
        ServiceCommand cmd = new ServiceCommand();
        cmd.setAction(action);
        return cmd;
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
