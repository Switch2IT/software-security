package be.ehb.switch2it.rest.config;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class AppConfig {

    private String configurationFile;
    private String version;
    private String buildDate;

    private String auth0JwksUri;
    private String auth0Audience;
    private String auth0Issuer;

    private String keycloakJwksUri;
    private String keycloakAudience;
    private String keycloakIssuer;

    public String getConfigurationFile() {
        return configurationFile;
    }

    public void setConfigurationFile(String configurationFile) {
        this.configurationFile = configurationFile;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getAuth0JwksUri() {
        return auth0JwksUri;
    }

    public void setAuth0JwksUri(String auth0JwksUri) {
        this.auth0JwksUri = auth0JwksUri;
    }

    public String getAuth0Audience() {
        return auth0Audience;
    }

    public void setAuth0Audience(String auth0Audience) {
        this.auth0Audience = auth0Audience;
    }

    public String getAuth0Issuer() {
        return auth0Issuer;
    }

    public void setAuth0Issuer(String auth0Issuer) {
        this.auth0Issuer = auth0Issuer;
    }

    public String getKeycloakJwksUri() {
        return keycloakJwksUri;
    }

    public void setKeycloakJwksUri(String keycloakJwksUri) {
        this.keycloakJwksUri = keycloakJwksUri;
    }

    public String getKeycloakAudience() {
        return keycloakAudience;
    }

    public void setKeycloakAudience(String keycloakAudience) {
        this.keycloakAudience = keycloakAudience;
    }

    public String getKeycloakIssuer() {
        return keycloakIssuer;
    }

    public void setKeycloakIssuer(String keycloakIssuer) {
        this.keycloakIssuer = keycloakIssuer;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "configurationFile='" + configurationFile + '\'' +
                ", version='" + version + '\'' +
                ", buildDate='" + buildDate + '\'' +
                ", auth0JwksUri='" + auth0JwksUri + '\'' +
                ", auth0Audience='" + auth0Audience + '\'' +
                ", auth0Issuer='" + auth0Issuer + '\'' +
                ", keycloakJwksUri='" + keycloakJwksUri + '\'' +
                ", keycloakAudience='" + keycloakAudience + '\'' +
                ", keycloakIssuer='" + keycloakIssuer + '\'' +
                '}';
    }
}