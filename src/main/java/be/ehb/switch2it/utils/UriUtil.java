package be.ehb.switch2it.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Guillaume Vandecasteele
 * @since 2018
 */
public final class UriUtil {

    private static final String X_FORWARD_PROTO_HEADER_NAME = "x-forwarded-proto";

    private UriUtil() {}

    public static String getRequestUri(HttpServletRequest servletRequest) throws URISyntaxException {
        String scheme = servletRequest.getHeader(X_FORWARD_PROTO_HEADER_NAME);
        if (StringUtils.isEmpty(scheme)) scheme = servletRequest.getScheme();
        int serverPort = servletRequest.getServerPort() == 80 ? -1 : servletRequest.getServerPort();
        URI uri = new URI(scheme, null, servletRequest.getServerName(), serverPort, servletRequest.getRequestURI(), servletRequest.getQueryString(), null);
        return uri.toString();
    }

}