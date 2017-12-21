package be.ehb.switch2it.rest.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public final class ExceptionFactory {

    private ExceptionFactory() {
    }

    public static ConfigurationException configurationNotRead(String configFile) {
        return new ConfigurationException("Configuration file not read: " + configFile, ErrorCodes.CONFIGURATION_NOT_READ);
    }

    public static UnsupportedKeySizeException unsupportedKeySizeException(String keySize) {
        return new UnsupportedKeySizeException("Specified key size is not supported: " + keySize);
    }
}