package be.ehb.switch2it.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorBean {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorBean withMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "ErrorBean{" +
                "message='" + message + '\'' +
                '}';
    }
}