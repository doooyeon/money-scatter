package com.pay.money.scatter.exception;

import com.pay.money.scatter.interfaces.response.RestExceptionView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> exception(final Exception e, final WebRequest request) {
        return exceptionToHttpErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(EntityDuplicationException.class)
    protected ResponseEntity<Object> entityDuplicationdException(EntityDuplicationException e, WebRequest request) {
        return exceptionToHttpErrorResponse(e, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> entityNotFoundException(EntityNotFoundException e, WebRequest request) {
        return exceptionToHttpErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    protected ResponseEntity<Object> unAuthorizedException(UnAuthorizedException e, WebRequest request) {
        return exceptionToHttpErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
    }

    private ResponseEntity<Object> exceptionToHttpErrorResponse(Exception e, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(e, RestExceptionView.of(status, e.getClass().getSimpleName(), e.getMessage()), null, status, request);
    }
}
