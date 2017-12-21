package be.ehb.switch2it.rest;

import io.swagger.models.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@WebServlet(name = "SwaggerJaxrsConfig", loadOnStartup = 5)
public class SwaggerJaxrsConfig extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) {
        try {
            super.init(servletConfig);

            //information
            Info info = new Info()
                    .title("Software Security API")
                    .description("Tool for EHB Software Security Course")
                    .contact(new Contact().email("guillaume.vandecasteele@student.ehb.be"))
                    .license(new License().name("GPL 3.0").url("http://www.gnu.org/licenses/"));
            ServletContext context = servletConfig.getServletContext();

            //configuration
            Swagger swagger = new Swagger().info(info);
            List<Scheme> schemes = new ArrayList<>();
            schemes.add(Scheme.HTTP);
            schemes.add(Scheme.HTTPS);
            swagger.schemes(schemes);
            swagger.host("localhost:8080");
            swagger.basePath("/software-security/v1");
            context.setAttribute("swagger", swagger);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}