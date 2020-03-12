package org.acme;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.quarkus.security.Authenticated;
import org.acme.models.User;

@Path("/user")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final User user;

    public UserResource() {
        user = new User();
        user.setEmail("ulysse@gmail.com");
        user.setName("maxime");
    }

    @GET
    public Response user(){
        return Response.ok(user).build();
    }
}

