package io.github.dongjulim.exception;

import io.github.dongjulim.domain.common.exception.DomainException;
import io.github.dongjulim.domain.common.exception.ErrorCode;
import io.github.dongjulim.model.ProblemDetail;
import io.github.dongjulim.security.exception.JwtExpiredTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    private static final Map<ErrorCode, HttpStatus> STATUS_MAP = Map.of(
            ErrorCode.USER_NOT_FOUND,         HttpStatus.NOT_FOUND,
            ErrorCode.PRODUCT_NOT_FOUND,      HttpStatus.NOT_FOUND,
            ErrorCode.NOTICE_NOT_FOUND,       HttpStatus.NOT_FOUND,
            ErrorCode.CART_NOT_FOUND,         HttpStatus.NOT_FOUND,
            ErrorCode.CART_ITEM_NOT_FOUND,    HttpStatus.NOT_FOUND,
            ErrorCode.NOTICE_LIKE_NOT_FOUND,  HttpStatus.NOT_FOUND,
            ErrorCode.ALREADY_LIKED,          HttpStatus.CONFLICT
    );

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> domainException(DomainException e, HttpServletRequest request) {
        log.error("DomainException [{}]: {}", e.getErrorCode(), e.getMessage());
        HttpStatus status = STATUS_MAP.getOrDefault(e.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);

        return ResponseEntity.status(status).body(
                ProblemDetail.builder()
                        .type(e.getErrorCode().getTypeUri())
                        .title(e.getErrorCode().getTitle())
                        .status(status.value())
                        .detail(e.getMessage())
                        .instance(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validationException(MethodArgumentNotValidException e,
                                                             HttpServletRequest request) {
        log.error("ValidationException: {}", e.getMessage());

        List<ProblemDetail.FieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> ProblemDetail.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ProblemDetail.builder()
                        .type("/api/errors/invalid-input")
                        .title("Invalid Input")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .detail("입력값 검증에 실패했습니다.")
                        .instance(request.getRequestURI())
                        .errors(fieldErrors)
                        .build()
        );
    }

    @ExceptionHandler(JwtExpiredTokenException.class)
    public ResponseEntity<ProblemDetail> jwtExpiredTokenException(JwtExpiredTokenException e,
                                                                   HttpServletRequest request) {
        log.error("JwtExpiredTokenException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ProblemDetail.builder()
                        .type("/api/errors/expired-token")
                        .title("Expired Token")
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .detail("토큰이 만료되었습니다.")
                        .instance(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> exception(Exception e, HttpServletRequest request) {
        log.error("UnhandledException: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ProblemDetail.builder()
                        .type("/api/errors/internal-server-error")
                        .title("Internal Server Error")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .detail("서버 내부 오류가 발생했습니다.")
                        .instance(request.getRequestURI())
                        .build()
        );
    }
}
