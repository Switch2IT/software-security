package be.ehb.switch2it.rest.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public final class ErrorCodes {

    private ErrorCodes() {}

    // HTTP status codes
    //
    public static final int HTTP_STATUS_CODE_INVALID_INPUT              = 400;
    public static final int HTTP_STATUS_CODE_UNAUTHORIZED               = 401;
    public static final int HTTP_STATUS_CODE_FORBIDDEN                  = 403;
    public static final int HTTP_STATUS_CODE_NOT_FOUND                  = 404;
    public static final int HTTP_STATUS_CODE_ALREADY_EXISTS             = 409;
    public static final int HTTP_STATUS_CODE_INVALID_STATE              = 409;
    public static final int HTTP_PRECONDITION_FAILURE                   = 412;
    public static final int HTTP_STATUS_CODE_SYSTEM_ERROR               = 500;

    public static final int CONFIGURATION_NOT_READ                      = 1001;

}