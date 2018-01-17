package be.ehb.switch2it.rest.resources;

import be.ehb.switch2it.rest.config.AppConfig;
import be.ehb.switch2it.rest.config.SoftwareSecurity;
import be.ehb.switch2it.rest.responses.SystemStatus;
import be.ehb.switch2it.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2018
 */
@Api(value = "System", description = "System related information")
@Path("/system/")
@ApplicationScoped
public class SystemResource {

    @SoftwareSecurity
    @Inject
    private AppConfig config;

    @GET
    @Path("status")
    @ApiOperation(value = "System Status", notes = "This endpoint returns the system status, comprised of version and build time")
    @ApiResponses({
            @ApiResponse(code = 200, response = SystemStatus.class, message = "Success")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublicResponse() {

        SystemStatus status = new SystemStatus();
        status.setBuildDate(TimeUtil.getFormattedDateTime(config.getBuildDate()));
        status.setVersion(config.getVersion());
        status.setStartUpDate(TimeUtil.getFormattedDateTime(config.getStartUpDate()));
        status.setUpTime(TimeUtil.getFormattedIntervalBetweenThenAndNow(config.getStartUpDate()));
        return Response.ok().entity(status).build();
    }
}