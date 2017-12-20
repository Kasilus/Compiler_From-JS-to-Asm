public class SyntacticException extends RuntimeException {

    public SyntacticException(String message, Position position) {

        super(message + "\t" + position);
    }

}
