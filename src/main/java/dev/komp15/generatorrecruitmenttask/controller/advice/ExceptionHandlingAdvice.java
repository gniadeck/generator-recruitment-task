package dev.komp15.generatorrecruitmenttask.controller.advice;

import dev.komp15.generatorrecruitmenttask.dto.ExceptionDTO;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler(OutOfMemoryError.class)
    public ResponseEntity<ExceptionDTO> handleOutOfMemoryError(){
        return ResponseEntity.internalServerError().body(
                new ExceptionDTO("Our system is overloaded. Please try again later.", 503));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleConstraintViolationException(ConstraintViolationException exception){
        ConstraintViolation<?> violation = exception.getConstraintViolations().stream()
                .findFirst()
                .orElseThrow();

        return ResponseEntity.badRequest().body(
                new ExceptionDTO("The request is not valid. Cause: " + violation.getPropertyPath().toString().strip() +
                        " " + violation.getMessage().strip(), 400)
        );
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ExceptionDTO> handleTransactionSystemException(TransactionSystemException transactionException){
        if(transactionException.getRootCause() instanceof ConstraintViolationException constraintException){
            return handleConstraintViolationException(constraintException);
        } else {
            return handleNotExpectedError(transactionException);
        }
    }

    @ExceptionHandler(Throwable.class)
    @Order(0)
    public ResponseEntity<ExceptionDTO> handleNotExpectedError(Throwable throwable){
        throwable.printStackTrace();
        return ResponseEntity.internalServerError().body(
                new ExceptionDTO("Unexpected error occurred. Message: " + throwable.getMessage(), 500)
        );
    }

}
