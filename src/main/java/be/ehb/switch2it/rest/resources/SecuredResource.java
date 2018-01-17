package be.ehb.switch2it.rest.resources;

import be.ehb.switch2it.rest.model.EndpointType;
import be.ehb.switch2it.rest.responses.GenericResponse;
import be.ehb.switch2it.utils.UriUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
@Api(value = "Software Security Course API", description = "Some endpoint require a valid JWT")
@Path("/api/")
@ApplicationScoped
public class SecuredResource {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Context
    private HttpServletRequest servletRequest;

    @GET
    @Path("public")
    @ApiOperation(value = "Public endpoint", notes = "This endpoint returns a string value composed of the date & time, endpoint type (public or private) and the request URL including any querystring parameters.")
    @ApiResponses({
            @ApiResponse(code = 200, response = GenericResponse.class, message = "Success"),
            @ApiResponse(code = 200, response = String.class, message = "Success")
    })
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
    public Response getPublicResponse() {
        return getApiResponse(EndpointType.PUBLIC);
    }


    @GET
    @Path("private")
    @ApiOperation(value = "Private Endpoint", notes = "This endpoint returns a string value composed of the date & time, endpoint type (public or private) and the request URL including any querystring parameters if a valid JWT was included in the request. Otherwise it returns a JSON error message prefixed by the date and time")
    @ApiResponses({
            @ApiResponse(code = 200, response = GenericResponse.class, message = "Success"),
            @ApiResponse(code = 200, response = String.class, message = "Success"),
            @ApiResponse(code = 401, response = GenericResponse.class, message = "Unauthorized")
    })
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
    public Response getPrivateResponse() {
        return getApiResponse(EndpointType.PRIVATE);
    }

    private Response getApiResponse(EndpointType endpointType) {
        Response response;
        String timeAndDate = SDF.format(new Date());
        String textToDisplay;
        try {
            textToDisplay = String.format("%s - %s API endpoint at: %s", timeAndDate, endpointType.getDescription(), UriUtil.getRequestUri(servletRequest));
        } catch (URISyntaxException ex) {
            textToDisplay = String.format("%s - Failure to reconstruct request URI: %s", timeAndDate, ex.getMessage());
        }
        String acceptHeader = servletRequest.getHeader("accept");
        if (acceptHeader != null && acceptHeader.contains(",")) {
            response = Response.ok().entity(textToDisplay).build();
        } else {
            try {
                MediaType type = MediaType.valueOf(acceptHeader);

                if (type.equals(MediaType.TEXT_PLAIN_TYPE) || type.equals(MediaType.TEXT_HTML_TYPE)) {
                    response = Response.ok().entity(textToDisplay).build();
                } else {
                    response = Response.ok().entity(new GenericResponse(textToDisplay)).build();
                }
            } catch (IllegalArgumentException ex) {
                response = Response.ok().entity(textToDisplay).build();
            }
        }
        return response;
    }
}