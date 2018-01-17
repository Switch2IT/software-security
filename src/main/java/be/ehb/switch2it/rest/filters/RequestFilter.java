package be.ehb.switch2it.rest.filters;

import be.ehb.switch2it.rest.config.AppConfig;
import be.ehb.switch2it.rest.config.SoftwareSecurity;
import be.ehb.switch2it.rest.responses.GenericResponse;
import be.ehb.switch2it.utils.JWTUtils;
import be.ehb.switch2it.utils.UriUtil;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class RequestFilter implements ContainerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final String JWT_HEADER_NAME = "Authorization";
    private static final String NO_ACCsESS = "Unauthorized";
    private static final String BEARER_PREFIX = "Bearer ";

    private static final String SECURED_ENDPOINTS = "/api/private";

    @SoftwareSecurity
    @Inject
    private AppConfig config;

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (requestContext.getUriInfo().getPath().startsWith(SECURED_ENDPOINTS)) {
            String tokenHeaderString = requestContext.getHeaderString(JWT_HEADER_NAME);
            if (StringUtils.isNotEmpty(tokenHeaderString)) {
                String token = tokenHeaderString.replaceFirst(BEARER_PREFIX, "").trim();
                try {
                    JwtContext unvalidatedContext = JWTUtils.consumeUnvalidatedToken(token);
                    if (unvalidatedContext != null && unvalidatedContext.getJwtClaims() != null && StringUtils.isNotBlank(unvalidatedContext.getJwtClaims().getIssuer())) {
                        JwtClaims unvalidatedClaims = unvalidatedContext.getJwtClaims();

                        if (!unvalidatedClaims.hasAudience() || StringUtils.isEmpty(unvalidatedClaims.getIssuer())) {
                            abort(requestContext);
                        }

                        JwtClaims validatedClaims = null;
                        HttpsJwks jwks;
                        if (config.getAuth0Issuer().equals(unvalidatedClaims.getIssuer())) {
                            jwks = new HttpsJwks(config.getAuth0JwksUri());
                            validatedClaims = JWTUtils.validateRSAToken(token, config.getAuth0Issuer(), config.getAuth0Audience(), jwks).getJwtClaims();
                        } else if (config.getKeycloakIssuer().equals(unvalidatedClaims.getIssuer())) {
                            jwks = new HttpsJwks((config.getKeycloakJwksUri()));
                            validatedClaims = JWTUtils.validateRSAToken(token, config.getKeycloakIssuer(), config.getKeycloakAudience(), jwks).getJwtClaims();
                        }
                        if (validatedClaims == null) {
                            abort(requestContext);
                        }
                    }
                } catch (InvalidJwtException | MalformedClaimException ex) {
                    log.error("Error Parsing JWT: ", ex);
                    abort(requestContext);
                }
            } else {
                abort(requestContext);
            }
        }
    }

    private void abort(ContainerRequestContext requestContext) {
        GenericResponse genericResponse = new GenericResponse();
        String timeAndDate = SDF.format(new Date());
        try {
            genericResponse.setResponseContent(String.format("%s - Unauthorized to access: %s", timeAndDate , UriUtil.getRequestUri(servletRequest)));
        } catch (URISyntaxException ex) {
            genericResponse.setResponseContent(String.format("%s - Unauthorized: Failure to reconstruct request URI: %s", timeAndDate, ex.getMessage()));
        }
        requestContext.abortWith(Response
                .status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .entity(genericResponse)
                .build());
    }
}