package be.ehb.switch2it.rest.exceptions;


/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ConfigurationException extends AbstractRestException {

    private final int error;

    public ConfigurationException() {
        error = 0;
    }

    public ConfigurationException(final String message, final int error) {
        super(message);
        this.error = error;
    }

    public ConfigurationException(final String message, final Throwable cause, final int error) {
        super(message, cause);
        this.error = error;
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_INVALID_STATE;
    }

    @Override
    public int getErrorCode() {
        return error;
    }

    @Override
    public String getMoreInfoUrl() {
        return "";
    }
}