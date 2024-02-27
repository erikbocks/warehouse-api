package com.bock.warehouseapi.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class RestResponse {

    public static class MyResponse {

        @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT-03", pattern = "dd-MM-yyyy HH:mm:ss")
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

    public ResponseEntity<Object> badRequest(String message) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MyResponse response = new MyResponse();

        response.setStatus(status.value());
        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }

}
