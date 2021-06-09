package com.company.interview.exception.order;

import com.company.interview.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderNotFoundAdvice {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> GoalNotFoundHandler(OrderNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
