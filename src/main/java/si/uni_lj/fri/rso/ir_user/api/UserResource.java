package si.uni_lj.fri.rso.ir_user.api;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import si.uni_lj.fri.rso.ir_user.cdi.Config;
import si.uni_lj.fri.rso.ir_user.cdi.UserDatabase;
import si.uni_lj.fri.rso.ir_user.models.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("users")
public class UserResource {
    @Inject
    private Config config;

    @GET
    @Path("/config")
    public Response config() {
        String response =
                "{\n" +
                "    \"endpointEnabled\": \"%b\"\n" +
                "}";
        response = String.format(response, config.getEndpointEnabled());
        return Response.ok(response).build();
    }

    @GET
    public Response getAllUsers() {
        if (ConfigurationUtil.getInstance().getBoolean("rest-config.endpoint-enabled").orElse(false)) {
            List<User> users = UserDatabase.getUsers();
            return Response.ok(users).build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"reason\": \"Endpoint disabled.\"}").build();
        }
    }

    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") String userId) {
        if (ConfigurationUtil.getInstance().getBoolean("rest-config.endpoint-enabled").orElse(false)) {
            User user = UserDatabase.getUser(userId);
            return user != null
                    ? Response.ok(user).build()
                    : Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"reason\": \"Endpoint disabled.\"}").build();
        }
    }

    @POST
    public Response addNewUser(User user) {
        if (ConfigurationUtil.getInstance().getBoolean("rest-config.endpoint-enabled").orElse(false)) {
            UserDatabase.addUser(user);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"reason\": \"Endpoint disabled.\"}").build();
        }
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        if (ConfigurationUtil.getInstance().getBoolean("rest-config.endpoint-enabled").orElse(false)) {
            UserDatabase.deleteUser(userId);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"reason\": \"Endpoint disabled.\"}").build();
        }
    }
}
