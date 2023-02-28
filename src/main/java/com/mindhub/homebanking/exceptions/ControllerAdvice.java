package com.mindhub.homebanking.exceptions;

import com.mindhub.homebanking.dtos.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String msg = error.getDefaultMessage();
            errors.put(field, msg);
        });
        return new ResponseEntity<>(new ResponseDTO(400, "Validation Error!", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ResponseDTO> handleResponseException(ResponseException ex) {
        return new ResponseEntity<>(new ResponseDTO(ex.getStatus(), ex.getMessage()),
                HttpStatus.valueOf(ex.getStatus()));
    }


}
