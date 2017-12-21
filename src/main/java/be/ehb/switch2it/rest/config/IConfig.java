package be.ehb.switch2it.rest.config;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IConfig {

    String PROP_FILE_DATE = "date";
    String PROP_FILE_VERSION = "version";
    String PROP_FILE_CONFIG_FILE = "configuration.file";

    String IDP_AUTH0_JWKS_URI = "software-security.idp.auth0.jwks-uri";
    String IDP_AUTH0_AUDIENCE = "software-security.idp.auth0.audience";
    String IDP_AUTH0_ISSUER = "software-security.idp.auth0.issuer";

    String IDP_KEYCLOAK_JWKS_URI = "software-security.idp.keycloak.jwks-uri";
    String IDP_KEYCLOAK_AUDIENCE = "software-security.idp.keycloak.audience";
    String IDP_KEYCLOAK_ISSUER = "software-security.idp.keycloak.issuer";
}
