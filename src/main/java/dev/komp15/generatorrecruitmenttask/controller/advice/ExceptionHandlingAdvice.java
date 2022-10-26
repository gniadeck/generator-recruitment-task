package dev.komp15.generatorrecruitmenttask.controller.advice;

import dev.komp15.generatorrecruitmenttask.dto.ExceptionDTO;
import dev.komp15.generatorrecruitmenttask.utils.exceptions.NotValidException;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler(OutOfMemoryError.class)
    public ResponseEntity<ExceptionDTO> handleOutOfMemoryError(){
        return ResponseEntity.internalServerError().body(
                new ExceptionDTO("Our system is overloaded. Please try again later.", 503));
    }

    @ExceptionHandler(NotValidException.class)
    public ResponseEntity<ExceptionDTO> handleNotValidException(){
        return ResponseEntity.badRequest().body(
                new ExceptionDTO("The request is not valid. Please check if jobSize is achiveable.", 400)
        );
    }

}
