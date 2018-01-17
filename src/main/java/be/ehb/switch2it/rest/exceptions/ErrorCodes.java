package be.ehb.switch2it.rest.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public final class ErrorCodes {

    //
    // HTTP status codes
    //

    public static final int HTTP_STATUS_CODE_INVALID_STATE              = 409;

    //
    // Configuration related
    //

    public static final int CONFIGURATION_NOT_READ                      = 1001;

    private ErrorCodes() {}
}