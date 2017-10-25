package si.uni_lj.fri.rso.ir_user.api;

import si.uni_lj.fri.rso.ir_user.cdi.UserDatabase;
import si.uni_lj.fri.rso.ir_user.models.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("users")
public class UserResource {

    @GET
    public Response getAllUsers() {
        List<User> users = UserDatabase.getUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") String userId) {
        User user = UserDatabase.getUser(userId);
        return user != null
                ? Response.ok(user).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response addNewUser(User user) {
        UserDatabase.addUser(user);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        UserDatabase.deleteUser(userId);
        return Response.noContent().build();
    }
}
