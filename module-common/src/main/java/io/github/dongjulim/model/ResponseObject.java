package io.github.dongjulim.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class ResponseObject<T> implements Serializable {

    private LocalDateTime timestamp;
    private String message;
    private int statusCode;
    private T body;

    @Builder
    public ResponseObject(
        LocalDateTime timestamp,
        String message,
        int statusCode,
        T body
    ){
        this.timestamp = timestamp;
        this.message = message;
        this.statusCode = statusCode;
        this.body = body;
    }
}
