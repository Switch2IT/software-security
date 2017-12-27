package be.ehb.switch2it.rest.resources;

import be.ehb.switch2it.rest.model.ErrorBean;
import be.ehb.switch2it.rest.responses.DomainResponse;
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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Api(value = "REST API", description = "Some endpoint require a valid JWT")
@Path("/api/")
@ApplicationScoped
public class SecuredResource {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Context
    private HttpServletRequest servletRequest;

    @GET
    @Path("public")
    @ApiOperation(value = "Get welcome page", notes = "This endpoint returns the index HTML welcome page.")
    @ApiResponses({
            @ApiResponse(code = 200, response = String.class, message = "Success")
    })
    @Produces(MediaType.TEXT_HTML)
    public Response getIndex() {
        return Response.ok().entity("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Private</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Private</h1>\n" +
                "<h2>Go Away!</h2>\n" +
                "</body>\n" +
                "</html>").build();
    }


    @GET
    @Path("private")
    @ApiOperation(value = "Get domain info", notes = "This endpoint returns the domain info if a valid JWT was included in the request.")
    @ApiResponses({
            @ApiResponse(code = 200, response = String.class, message = "Success"),
            @ApiResponse(code = 401, response = ErrorBean.class, message = "Unauthorized")
    })
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
    public Response getHostname() {
        Response response;
        String textToDisplay = String.format("%s - %s", SDF.format(new Date()), servletRequest.getServerName());
        String acceptHeader = servletRequest.getHeader("accept");
        if (acceptHeader != null && acceptHeader.contains(",")) {
            response = Response.ok().entity(textToDisplay).build();
        } else {
            try {
                MediaType type = MediaType.valueOf(acceptHeader);

                if (type.equals(MediaType.TEXT_PLAIN_TYPE) || type.equals(MediaType.TEXT_HTML_TYPE)) {
                    response = Response.ok().entity(textToDisplay).build();
                } else {
                    response = Response.ok().entity(new DomainResponse(textToDisplay)).build();
                }
            } catch (IllegalArgumentException ex) {
                response = Response.ok().entity(textToDisplay).build();
            }
        }
        return response;
    }
}