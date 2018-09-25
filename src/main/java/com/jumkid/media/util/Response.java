package com.jumkid.media.util;

import java.util.ArrayList;

public class Response {

    boolean success = true;

    Long total = 0L;

    ArrayList<String> errors;

    Object data;

    public Response(){
        //void
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void addError(String error){
        if(this.errors==null){
            errors = new ArrayList<String>();
        }
        errors.add(error);
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

}
