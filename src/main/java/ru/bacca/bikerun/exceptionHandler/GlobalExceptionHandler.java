package ru.bacca.bikerun.exceptionHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.bacca.bikerun.exceptionHandler.exception.UsernamePasswordNullException;

import java.lang.reflect.InvocationTargetException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {
            UsernamePasswordNullException.class
    })
    protected ResponseEntity<?> usernameOrPasswordHandler(RuntimeException ex, WebRequest request) {
        String body = "Username and password might not be NULL! Please check input data or parameter name.";
        LOGGER.error("error", ex);
        ex.printStackTrace();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {
            NullPointerException.class
    })
    protected ResponseEntity<?> nullPointerHandler(RuntimeException ex, WebRequest request) {
        String body = "Some parameter is NULL!.";
        LOGGER.error("error", ex);
        ex.printStackTrace();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

     @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            InvocationTargetException.class,
            MultipartException.class,
    })
    protected ResponseEntity parameterExceptionHandler(RuntimeException ex, WebRequest request) {
        String body = "Parameter exception!";
        LOGGER.error("Error", ex);
        ex.printStackTrace();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {
            Exception.class
    })
    protected ResponseEntity handleException(RuntimeException ex, WebRequest request) {
        String body = "Unhandled exception!";
        LOGGER.error("Error", ex);
        ex.printStackTrace();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
