package com.custom.gateway.config.exception;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;

public class TooManyRequestsException extends ResponseStatusException {

    @Nullable
    private final Method handlerMethod;

    @Nullable
    private final MethodParameter parameter;


    /**
     * Constructor for a 500 error with a reason and an optional cause.
     * @since 5.0.5
     */
    public TooManyRequestsException(String reason, @Nullable Throwable cause) {
        super(HttpStatus.TOO_MANY_REQUESTS, reason, cause);
        this.handlerMethod = null;
        this.parameter = null;
    }

    /**
     * Constructor for a 500 error with a handler {@link Method} and an optional cause.
     * @since 5.0.5
     */
    public TooManyRequestsException(String reason, Method handlerMethod, @Nullable Throwable cause) {
        super(HttpStatus.TOO_MANY_REQUESTS, reason, cause);
        this.handlerMethod = handlerMethod;
        this.parameter = null;
    }

    /**
     * Constructor for a 500 error with a {@link MethodParameter} and an optional cause.
     */
    public TooManyRequestsException(String reason, MethodParameter parameter, @Nullable Throwable cause) {
        super(HttpStatus.TOO_MANY_REQUESTS, reason, cause);
        this.handlerMethod = parameter.getMethod();
        this.parameter = parameter;
    }

    /**
     * Constructor for a 500 error linked to a specific {@code MethodParameter}.
     */
    @Deprecated
    public TooManyRequestsException(String reason, MethodParameter parameter) {
        this(reason, parameter, null);
    }

    /**
     * Constructor for a 500 error with a reason only.
     */
    public TooManyRequestsException(String reason) {
        super(HttpStatus.TOO_MANY_REQUESTS, reason, null);
        this.handlerMethod = null;
        this.parameter = null;
    }


    /**
     * Return the handler method associated with the error, if any.
     * @since 5.0.5
     */
    @Nullable
    public Method getHandlerMethod() {
        return this.handlerMethod;
    }

    /**
     * Return the specific method parameter associated with the error, if any.
     */
    @Nullable
    public MethodParameter getMethodParameter() {
        return this.parameter;
    }
}
