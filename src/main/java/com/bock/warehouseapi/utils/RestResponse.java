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
public class RestResponse {

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private static class MyResponse {
        private Date timestamp;
        private Integer status;
        private List<String> messages;
        private Object result;

        public MyResponse() {
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
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private static class MyErrorResponse {
        private Date timestamp;
        private Integer status;
        private List<String> methods;
        private List<String> messages;
        private List<ErrorMessageDTO> errors;

        public MyErrorResponse() {
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
    }

    public ResponseEntity<Object> ok(String message, Object object) {
        HttpStatus status = HttpStatus.OK;
        MyResponse response = new MyResponse();

        response.setMessage(message);
        response.setResult(object);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> ok(String message) {
        HttpStatus status = HttpStatus.OK;
        MyResponse response = new MyResponse();

        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> created(String message) {
        HttpStatus status = HttpStatus.CREATED;
        MyResponse response = new MyResponse();

        response.setStatus(status.value());
        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> badRequest(List<String> messages) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MyErrorResponse response = new MyErrorResponse();

        response.setStatus(status.value());
        response.setMessages(messages);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> badRequest(String message) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MyErrorResponse response = new MyErrorResponse();

        response.setStatus(status.value());
        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> badRequest(String message, List<ErrorMessageDTO> errors) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MyErrorResponse response = new MyErrorResponse();

        response.setMessage(message);
        response.setErrors(errors);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> unauthorized(String message) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        MyErrorResponse response = new MyErrorResponse();

        response.setStatus(status.value());
        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> notFound(String message) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        MyErrorResponse response = new MyErrorResponse();

        response.setStatus(status.value());
        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<Object> internalServerError(String message) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        MyErrorResponse response = new MyErrorResponse();

        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

}
