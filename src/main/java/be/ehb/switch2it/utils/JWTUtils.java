package be.ehb.switch2it.utils;

import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;

/**
 * Created by Guillaume Vandecasteele on 19/11/15.
 */
public class JWTUtils {

    /**
     * Validate token signed with RSA algorithm.
     *
     * @param jwt
     * @param expectedIssuer
     * @param jwks
     * @return
     * @throws InvalidJwtException
     */
    public static JwtContext validateRSAToken(String jwt, String expectedIssuer, String expectedAudience, HttpsJwks jwks) throws InvalidJwtException {
        JwtContext context = null;
        JwtConsumerBuilder builder = new JwtConsumerBuilder()
                .setExpectedIssuer(expectedIssuer)
                .setRequireSubject()
                .setAllowedClockSkewInSeconds(30)
                .setRequireExpirationTime()
                .setExpectedAudience(expectedAudience)
                //.setSkipDefaultAudienceValidation()
                .setVerificationKeyResolver(new HttpsJwksVerificationKeyResolver(jwks));
        context = builder.build().process(jwt);
        return context;
    }

    /**
     * We just want to consume the claims
     *
     * @param jwt
     * @return
     * @throws InvalidJwtException
     */
    public static JwtContext consumeUnvalidatedToken(String jwt) throws InvalidJwtException {
        return getUnvalidatedContext(jwt);
    }

    private static JwtContext getUnvalidatedContext(String jwt) throws InvalidJwtException {
        return new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build()
                .process(jwt);
    }
}
