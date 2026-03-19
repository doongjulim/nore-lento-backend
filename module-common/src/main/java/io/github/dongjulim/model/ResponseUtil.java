package io.github.dongjulim.model;

import org.springframework.http.HttpStatus;

@SuppressWarnings("unchecked")
public class ResponseUtil {

    public static <T> ResponseObject<T> success() {
        return success("SUCCESS!", null);
    }
    public static <T> ResponseObject<T> successToMsg(String msg) {
        return success(msg, null);
    }
    public static <T> ResponseObject<T> success(T body) {
        return success( "SUCCESS!", body);
    }

    public static <T> ResponseObject<T> success(String msg, T body) {
        return (ResponseObject<T>) ResponseObject.builder()
                .body(body)
                .message(msg)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    public static <T> ResponseObject<T> failure(String msg, HttpStatus httpStatus) {
        return (ResponseObject<T>) ResponseObject.builder()
                .message(msg)
                .statusCode(httpStatus.value())
                .build();
    }
}
