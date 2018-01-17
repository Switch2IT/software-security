package be.ehb.switch2it.rest.config;

import be.ehb.switch2it.rest.exceptions.ExceptionFactory;
import be.ehb.switch2it.utils.TimeUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Singleton
public class ConfigParser {

    private static final Logger log = LoggerFactory.getLogger(ConfigParser.class);

    private static final String APP_PROPERTIES = "application.properties";

    private Config config;
    private Properties properties;
    private transient AppConfig appConfig = new AppConfig();

    @PostConstruct
    public void init() {
        //read properties file
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(APP_PROPERTIES)) {
            properties = new Properties();
            if (is != null) {
                properties.load(is);
            } else throw ExceptionFactory.configurationNotRead(APP_PROPERTIES);
        } catch (IOException ex) {
            throw ExceptionFactory.configurationNotRead(APP_PROPERTIES);
        }
        //read specific application config, depends on the maven profile that has been set
        config = ConfigFactory.parseFile(new File(getConfigurationFile()));
        if (config == null) {
            throw ExceptionFactory.configurationNotRead(getConfigurationFile());
        } else {

            appConfig.setConfigurationFile(getConfigurationFile());
            appConfig.setBuildDate(getBuildDate());
            appConfig.setStartUpDate(DateTime.now());
            appConfig.setVersion(getVersion());
            appConfig.setAuth0JwksUri(getAuth0JwksUri());
            appConfig.setAuth0Issuer(getAuth0Issuer());
            appConfig.setAuth0Audience(getAuth0Audience());
            appConfig.setKeycloakJwksUri(getKeycloakJwksUri());
            appConfig.setKeycloakIssuer(getKeycloakIssuer());
            appConfig.setKeycloakAudience(getKeycloakAudience());

            log.info("=================== Software Security Configuration ===================================");
            log.info("General - Using configuration file: {}", appConfig.getConfigurationFile());
            log.info("General - Build date-time: {}", TimeUtil.getFormattedDateTime(appConfig.getBuildDate()));
            log.info("General - Version: {}", appConfig.getVersion());
            log.info("General - Startup date-time: {}", TimeUtil.getFormattedDateTime(appConfig.getStartUpDate()));
            log.info("IDP - Auth0 - JWKS URI: {}", appConfig.getAuth0JwksUri());
            log.info("IDP - Auth0 - Issuer: {}", appConfig.getAuth0Issuer());
            log.info("IDP - Auth0 - Audience: {}", appConfig.getAuth0Audience());
            log.info("IDP - Keycloak - JWKS URI: {}", appConfig.getKeycloakJwksUri());
            log.info("IDP - Keycloak - Issuer: {}", appConfig.getKeycloakIssuer());
            log.info("IDP - Keycloak - Audience: {}", appConfig.getKeycloakAudience());
            log.info("=======================================================================================");
        }
    }

    @SoftwareSecurity
    @Produces
    public AppConfig getAppConfig() {
        return appConfig;
    }

    private String getConfigurationFile() {
        return properties.getProperty(IConfig.PROP_FILE_CONFIG_FILE);
    }

    private String getVersion() {
        return properties.getProperty(IConfig.PROP_FILE_VERSION);
    }

    private DateTime getBuildDate() {
        return TimeUtil.convertBuildTimeToDateTime(properties.getProperty(IConfig.PROP_FILE_DATE));
    }

    private String getAuth0JwksUri() {
        return getConfigStringProperty(IConfig.IDP_AUTH0_JWKS_URI);
    }

    private String getAuth0Audience() {
        return getConfigStringProperty(IConfig.IDP_AUTH0_AUDIENCE);
    }

    private String getAuth0Issuer() {
        return getConfigStringProperty(IConfig.IDP_AUTH0_ISSUER);
    }

    private String getKeycloakJwksUri() {
        return getConfigStringProperty(IConfig.IDP_KEYCLOAK_JWKS_URI);
    }

    private String getKeycloakAudience() {
        return getConfigStringProperty(IConfig.IDP_KEYCLOAK_AUDIENCE);
    }

    private String getKeycloakIssuer() {
        return getConfigStringProperty(IConfig.IDP_KEYCLOAK_ISSUER);
    }

    private String getConfigStringProperty(String propKey) {
        try {
            return config.getString(propKey);
        } catch (ConfigException ex) {
            log.error("Missing property: {}", propKey);
            return null;
        }
    }
}