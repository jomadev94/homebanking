package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public class ResponseDTO {

    private int status=200;
    private String message="Operation success";
    private Object data;

    public ResponseDTO(){}

    public ResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseDTO(int status, String message, Object data) {
        this(status, message);
        this.data = data;
    }

    public ResponseDTO(int status, Object data) {
        this.data=data;
        this.status=status;
    }

    public ResponseDTO(Object data) {
        this.data=data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
