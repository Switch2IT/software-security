package be.ehb.switch2it.rest.resources;

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
import java.io.InputStream;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Api(value = "Domain Info", description = "Assessment-related endpoints")
@Path("/")
@ApplicationScoped
public class AssessmentResource {

    private static final String TYPE_ICON = "image/x-icon";
    private static final String HEADER_CONTENT = "Content-Type";

    @Context
    private HttpServletRequest servletRequest;

    @GET
    @ApiOperation(value = "Welcome message", notes = "Returns a welcome message for whomever vists the website")
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
    @Path("assess_me")
    @ApiOperation(value = "Returns the domain", notes = "Returns the domain currently hosting the application")
    @ApiResponses({
            @ApiResponse(code = 200, response = String.class, message = "Success")
    })
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
    public Response getHostname() {
        Response response;
        String acceptHeader = servletRequest.getHeader("accept");
        if (acceptHeader != null && acceptHeader.contains(",")) {
            response = Response.ok().entity(servletRequest.getServerName()).build();
        } else {
            try {
                MediaType type = MediaType.valueOf(acceptHeader);
                if (type.equals(MediaType.TEXT_PLAIN_TYPE) || type.equals(MediaType.TEXT_HTML_TYPE)) {
                    response = Response.ok().entity(servletRequest.getServerName()).build();
                } else {
                    response = Response.ok().entity(new DomainResponse(servletRequest.getServerName())).build();
                }
            } catch (IllegalArgumentException ex) {
                response = Response.ok().entity(servletRequest.getServerName()).build();
            }
        }
        return response;
    }

    @GET
    @ApiOperation(value = "Returns a favicon", notes = "Returns a favicon to display with the welcome page")
    @Path("favicon.ico")
    @ApiResponses({
            @ApiResponse(code = 200, response = InputStream.class, message = "Success")
    })
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response getFavicon() {
        try {
            InputStream is = this.getClass().getResourceAsStream("/favicon-32x32.png");
            return Response.ok().header(HEADER_CONTENT, TYPE_ICON).entity(is).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}