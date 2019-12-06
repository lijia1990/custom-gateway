package com.custom.gateway.model.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@Accessors(chain = true)
public class ResponseBodyEntity<T> {

    private Integer code;

    private String message;

    private Boolean success;

    private T data;

    public ResponseBodyEntity(HttpStatus httpStatus, Boolean success) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.success = success;
    }

    public ResponseBodyEntity(Exception e) {
        this.code = HttpStatus.BAD_REQUEST.value();
        this.message = e.getMessage();
        this.success = false;
    }

    public ResponseBodyEntity(String e) {
        this.code = HttpStatus.BAD_REQUEST.value();
        this.message = e;
        this.success = false;
    }

    public ResponseBodyEntity(HttpStatus httpStatus, Boolean success, T data) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.success = success;
        this.data = data;
    }

    public static ResponseBodyEntity success() {
        return new ResponseBodyEntity(HttpStatus.OK, true);
    }

    public static ResponseBodyEntity success(Object data) {
        return new ResponseBodyEntity(HttpStatus.OK, true, data);
    }

    public static ResponseBodyEntity initFail() {
        return new ResponseBodyEntity(HttpStatus.BAD_REQUEST, false);
    }

    public static ResponseBodyEntity initFail(Object data) {
        return new ResponseBodyEntity(HttpStatus.BAD_REQUEST, false);
    }


}
