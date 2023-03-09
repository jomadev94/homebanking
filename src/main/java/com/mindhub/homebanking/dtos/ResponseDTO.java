package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public class ResponseDTO {

    private int status;
    private String message;
    private Object data;

    public ResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseDTO(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseDTO(int status, Object data) {
        this.status = status;
        this.message = "Operation success";
        this.data = data;
    }

    public ResponseDTO(Object data) {
        this.status = 200;
        this.message = "Operation success";
        this.data = data;
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
