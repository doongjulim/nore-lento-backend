package io.github.dongjulim.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * RFC 9457 Problem Details for HTTP APIs
 * https://www.rfc-editor.org/rfc/rfc9457
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {

    /** URI reference that identifies the problem type. */
    private final String type;

    /** Short, human-readable summary of the problem type. */
    private final String title;

    /** HTTP status code. */
    private final int status;

    /** Human-readable explanation specific to this occurrence of the problem. */
    private final String detail;

    /** URI reference that identifies the specific occurrence of the problem. */
    private final String instance;

    /** Extended field: field-level validation errors. */
    private final List<FieldError> errors;

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String message;
    }
}
