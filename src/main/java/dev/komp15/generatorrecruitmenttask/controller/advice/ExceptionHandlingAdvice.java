package dev.komp15.generatorrecruitmenttask.controller.advice;

import dev.komp15.generatorrecruitmenttask.dto.ExceptionDTO;
import dev.komp15.generatorrecruitmenttask.utils.exceptions.NotValidException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Priority;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler(OutOfMemoryError.class)
    public ResponseEntity<ExceptionDTO> handleOutOfMemoryError(){
        return ResponseEntity.internalServerError().body(
                new ExceptionDTO("Our system is overloaded. Please try again later.", 503));
    }

    @ExceptionHandler(NotValidException.class)
    public ResponseEntity<ExceptionDTO> handleNotValidException(NotValidException e){
        return ResponseEntity.badRequest().body(
                new ExceptionDTO("The request is not valid. Cause: " + e.getMessage(), 400)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleConstraintViolationException(ConstraintViolationException e){
        ConstraintViolation<?> violation = e.getConstraintViolations().stream().findFirst().orElseThrow();
        return ResponseEntity.badRequest().body(
                new ExceptionDTO("The request is not valid. Cause: " + violation.getPropertyPath() +
                        " " + violation.getMessage(), 400)
        );
    }

    @ExceptionHandler(Throwable.class)
    @Order(0)
    public ResponseEntity<ExceptionDTO> handleNotExpectedError(Throwable t){
        return ResponseEntity.internalServerError().body(
                new ExceptionDTO("Unexpected error occured. Message: " + t.getMessage(), 500)
        );
    }



}
