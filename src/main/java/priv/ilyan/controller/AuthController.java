package priv.ilyan.controller;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import priv.ilyan.config.Encoder;
import priv.ilyan.config.Token;
import priv.ilyan.model.AuthRequest;
import priv.ilyan.model.AuthResponse;
import priv.ilyan.model.User;

@Path("/auth")
public class AuthController {

    @Inject
    Encoder encoder;
    @ConfigProperty(name = "custom.quarkusjwt.jwt.duration") public Long duration;
	@ConfigProperty(name = "mp.jwt.verify.issuer") public String issuer;

	@PermitAll
	@POST @Path("/login") @Produces(MediaType.APPLICATION_JSON)
	public Response login(AuthRequest authRequest) {
		User u = User.findByUsername(authRequest.getUsername());
		if (u != null && u.password.equals(encoder.encode(authRequest.getPassword()))) {
			try {
				return Response.ok(new AuthResponse(Token.generateToken(u.username, u.roles, duration, issuer))).build();
			} catch (Exception e) {
				return Response.status(Status.UNAUTHORIZED).build();
			}
		} else {
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}
}
