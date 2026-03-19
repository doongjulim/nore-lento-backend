package io.github.dongjulim.exception;

import io.github.dongjulim.model.ResponseObject;
import io.github.dongjulim.security.exception.JwtExpiredTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


import static io.github.dongjulim.model.ResponseUtil.failure;

@RestController
@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(JwtExpiredTokenException.class)
    public final ResponseEntity<ResponseObject<Void>> jwtExpiredTokenException(JwtExpiredTokenException e){
        log.error("jwtExpiredTokenException : ", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED.value())
                .body(failure("토큰 기간 만료",HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ResponseObject<Void>> illegalArgumentException(IllegalArgumentException e){
        log.error("illegalArgumentException : ", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED.value())
                .body(failure(e.getMessage(), HttpStatus.UNAUTHORIZED));
    }
}
