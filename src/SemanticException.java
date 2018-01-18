public class SemanticException extends RuntimeException {

    public SemanticException(String message, Position position) {

        super(message + "\t" + position);
    }

}
