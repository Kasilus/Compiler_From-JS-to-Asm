package errors;

public class SyntacticException extends RuntimeException {

    public SyntacticException(String message, ExceptionPosition position) {

        super(message + "\t" + position);
    }

}
