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

import java.lang.reflect.InvocationTargetException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            InvocationTargetException.class,
            MultipartException.class,
            NullPointerException.class
    })
    protected ResponseEntity<?> handleException(RuntimeException ex, WebRequest request) {
        String body = "Something wrong...";
        LOGGER.error("Error", ex);
        ex.printStackTrace();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
