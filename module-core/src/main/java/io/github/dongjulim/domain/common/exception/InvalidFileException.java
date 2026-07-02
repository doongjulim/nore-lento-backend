package io.github.dongjulim.domain.common.exception;

public class InvalidFileException extends DomainException {

    public InvalidFileException(String message) {
        super(ErrorCode.INVALID_FILE, message);
    }
}
