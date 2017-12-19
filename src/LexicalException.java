public class LexicalException extends RuntimeException {

    public LexicalException(String message, Position position) {

        super(message + "\t" + position);
    }


}
