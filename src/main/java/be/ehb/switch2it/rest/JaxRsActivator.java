package be.ehb.switch2it.rest;

import be.ehb.switch2it.rest.filters.RequestFilter;
import be.ehb.switch2it.rest.resources.AssessmentResource;
import be.ehb.switch2it.rest.resources.SecuredResource;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationPath("/v1")
public class JaxRsActivator extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(RequestFilter.class);
        resources.add(ApiListingResource.class);
        resources.add(SwaggerSerializers.class);
        resources.add(AssessmentResource.class);
        resources.add(SecuredResource.class);
        return resources;
    }
}