package be.ehb.switch2it.rest.responses;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@XmlRootElement
public class DomainResponse {

    private String domain;

    public DomainResponse() {
    }

    public DomainResponse(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}