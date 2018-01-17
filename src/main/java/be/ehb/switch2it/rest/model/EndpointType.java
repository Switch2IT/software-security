package be.ehb.switch2it.rest.model;

/**
 * @author Guillaume Vandecasteele
 * @since 2018
 */
public enum EndpointType {

    PRIVATE("Private"), PUBLIC("Public");

    private String description;

    EndpointType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
