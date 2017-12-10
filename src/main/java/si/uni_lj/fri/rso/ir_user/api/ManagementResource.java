package si.uni_lj.fri.rso.ir_user.api;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Metered;
import si.uni_lj.fri.rso.ir_user.cdi.Config;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("mgmt")
@Log
public class ManagementResource {
    @Inject
    private Config config;

    private Logger log = Logger.getLogger(ManagementResource.class.getName());

    @GET
    @Metered
    @Path("/info")
    public Response milestoneInformation() {
        // TODO: enter the ips and ports
        String ip = "TODO";
        int userServicePort = 12345;
        int propertyCatalogueServicePort = 12345;

        String githubBase = "https://github.com/IglooRental/";
        String travisBase = "https://travis-ci.org/IglooRental/";
        String dockerhubBase = "https://hub.docker.com/r/jm5619/";

        HashMap<String, Object> result = new HashMap<>();
        result.put("clani", Arrays.asList("jm5619", "ss3055"));
        result.put("opis_projekta", "Domena projekta je portal za najemanje iglujev.");
        result.put("mikrostoritve", Arrays.asList(
                String.format("http://%s:%d/v1/users/", ip, userServicePort),
                String.format("http://%s:%d/v1/properties/", ip, propertyCatalogueServicePort)
        ));
        result.put("github", Arrays.asList(
                githubBase + "igloorental",
                githubBase + "ir-user",
                githubBase + "ir-property-catalogue"
        ));
        result.put("travis", Arrays.asList(
                travisBase + "ir-user",
                travisBase + "ir-property-catalogue"
        ));
        result.put("dockerhub", Arrays.asList(
                dockerhubBase + "ir-user",
                dockerhubBase + "ir-property-catalogue"
        ));

        return Response.ok(result).build();
    }

    @GET
    @Metered
    @Path("/config")
    public Response config() {
        String response =
                "{\n" +
                "    \"endpointEnabled\": \"%b\",\n" +
                "    \"healthy\": \"%b\",\n" +
                "    \"loadIntensity\": \"%d\"\n" +
                "}";
        response = String.format(response, config.getEndpointEnabled(), config.getHealthy(), config.getLoadIntensity());
        return Response.ok(response).build();
    }

    @POST
    @Metered
    @Path("/sethealth")
    public Response setHealth(Boolean health) {
        config.setHealthy(health);
        log.info(String.format("Manually set health: %b", health));
        return Response.ok().build();
    }

    @POST
    @Metered
    @Path("/load")
    public Response loadOrder() {
        long result = fibonacci(config.getLoadIntensity());
        log.warning(String.format("Loading service with fibonacci: intensity %d", result));
        return Response.status(Response.Status.OK).build();
    }

    private long fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
