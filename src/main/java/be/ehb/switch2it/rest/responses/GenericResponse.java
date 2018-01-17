package be.ehb.switch2it.rest.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {

    private String responseContent;

    public GenericResponse() {
    }

    public GenericResponse(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }
}