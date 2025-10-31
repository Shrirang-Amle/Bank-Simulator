package api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    public static class LoginRequest {
        public String username;
        public String password;
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest req) {
        try {
            if (req == null || req.username == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"Invalid request\"}").build();
            }
            // Simple dev-only hard-coded check. Change for production.
            if ("admin".equals(req.username) && "Adm1n$ecure!2025".equals(req.password)) {
                Map<String, String> map = new HashMap<>();
                map.put("token", "mock-admin-token");
                return Response.ok(map).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\":\"Invalid credentials\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Server error\"}")
                    .build();
        }
    }
}
