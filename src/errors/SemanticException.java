package errors;

public class SemanticException extends RuntimeException {

    public SemanticException(String message, ExceptionPosition position) {

        super(message + "\t" + position);
    }

}
