package si.uni_lj.fri.rso.ir_user.api;

import org.eclipse.microprofile.metrics.annotation.Metered;
import si.uni_lj.fri.rso.ir_user.cdi.Config;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("mgmt")
public class ManagementResource {
    @Inject
    private Config config;

    @GET
    @Metered
    @Path("/config")
    public Response config() {
        String response =
                "{\n" +
                "    \"endpointEnabled\": \"%b\",\n" +
                "    \"healthy\": \"%b\"\n" +
                "}";
        response = String.format(response, config.getEndpointEnabled(), config.getHealthy());
        return Response.ok(response).build();
    }

    @POST
    @Metered
    @Path("/sethealth")
    public Response setHealth(Boolean health) {
        config.setHealthy(health);
        return Response.ok().build();
    }

    @POST
    @Metered
    @Path("/load")
    public Response loadOrder() {
        long result = fibonacci(config.getLoadIntensity());
        return Response.status(Response.Status.OK).build();
    }

    private long fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
