package com.bock.warehouseapi.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RestResponse {
    private Date timestamp;
    private Integer status;
    private List<String> messages;
    private Object result;

    public RestResponse() {
        this.timestamp = new Date();
        this.status = HttpStatus.OK.value();
        this.messages = new ArrayList<>();
        this.result = null;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void setMessage(String message) {
        getMessages().add(message);
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public ResponseEntity<Object> ok(String message, Object object) {
        HttpStatus status = HttpStatus.OK;
        RestResponse response = new RestResponse();

        response.setMessage(message);
        response.setResult(object);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> ok(String message) {
        HttpStatus status = HttpStatus.OK;
        RestResponse response = new RestResponse();

        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> created(String message) {
        HttpStatus status = HttpStatus.CREATED;
        RestResponse response = new RestResponse();

        response.setStatus(status.value());
        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }
}
