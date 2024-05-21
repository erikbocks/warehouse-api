package com.bock.warehouseapi.utils;

import com.bock.warehouseapi.entities.dtos.ErrorMessageDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RestErrorResponse {
    private Date timestamp;
    private Integer status;
    private List<String> methods;
    private List<String> messages;
    private List<ErrorMessageDTO> errors;

    public RestErrorResponse() {
        this.timestamp = new Date();
        this.methods = null;
        this.status = HttpStatus.BAD_REQUEST.value();
        this.messages = new ArrayList<>();
        this.errors = null;
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

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
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

    public List<ErrorMessageDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorMessageDTO> errors) {
        this.errors = errors;
    }

    public ResponseEntity<Object> badRequest(List<String> messages) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        RestErrorResponse response = new RestErrorResponse();

        response.setStatus(status.value());
        response.setMessages(messages);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> badRequest(String message) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        RestErrorResponse response = new RestErrorResponse();

        response.setStatus(status.value());
        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> badRequest(String message, List<ErrorMessageDTO> errors) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        RestErrorResponse response = new RestErrorResponse();

        response.setMessage(message);
        response.setErrors(errors);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> unauthorized(String message) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        RestErrorResponse response = new RestErrorResponse();

        response.setStatus(status.value());
        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> notFound(String message) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        RestErrorResponse response = new RestErrorResponse();

        response.setStatus(status.value());
        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> internalServerError(String message) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        RestErrorResponse response = new RestErrorResponse();

        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }
}
