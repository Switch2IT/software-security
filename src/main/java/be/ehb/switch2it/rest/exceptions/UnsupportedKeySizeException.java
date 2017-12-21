package be.ehb.switch2it.rest.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UnsupportedKeySizeException extends RuntimeException {

    public UnsupportedKeySizeException(String message) {
        super(message);
    }
}