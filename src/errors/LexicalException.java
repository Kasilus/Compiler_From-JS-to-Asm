package errors;

public class LexicalException extends RuntimeException {

    public LexicalException(String message, ExceptionPosition position) {

        super(message + "\t" + position);
    }


}
