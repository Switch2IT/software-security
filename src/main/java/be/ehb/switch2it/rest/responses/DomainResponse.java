package be.ehb.switch2it.rest.responses;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@XmlRootElement
public class DomainResponse {

    private String timeAndDomain;

    public DomainResponse() {
    }

    public DomainResponse(String timeAndDomain) {
        this.timeAndDomain = timeAndDomain;
    }

    public String getTimeAndDomain() {
        return timeAndDomain;
    }

    public void setTimeAndDomain(String timeAndDomain) {
        this.timeAndDomain = timeAndDomain;
    }
}