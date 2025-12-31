package io.github.tato126.board.common.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String resourceName, Long id) {
        super(resourceName + " not found: " + id);
    }
}
