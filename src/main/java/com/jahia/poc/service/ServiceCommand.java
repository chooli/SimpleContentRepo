package com.jahia.poc.service;

/**
 * a command object to host service param and result
 */

import java.util.HashMap;

public class ServiceCommand {

    private String action;

    private HashMap<String, Object> params;

    private HashMap<String, Object> results;

    private HashMap<String, Object> errors;

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public void addParam(String key, Object value){
        if(params==null){
            params = new HashMap<String, Object>();
        }
        params.put(key, value);
    }

    public Object getParam(String key){
        return params != null ? params.get(key):null;
    }

    public HashMap<String, Object> getResults() {
        return results;
    }

    public void setResults(HashMap<String, Object> results) {
        this.results = results;
    }

    public void addResult(String key, Object value){
        if(results==null){
            results = new HashMap<String, Object>();
        }
        results.put(key, value);
    }

    public Object getResult(String key){
        return results != null ? results.get(key):null;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public HashMap<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, Object> errors) {
        this.errors = errors;
    }

    public void addError(String key, Object value){
        if(errors==null){
            errors = new HashMap<String, Object>();
        }
        errors.put(key, value);
    }

}
