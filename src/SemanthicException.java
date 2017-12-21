public class SemanthicException extends RuntimeException {

    public SemanthicException(String message, Position position) {

        super(message + "\t" + position);
    }

}
