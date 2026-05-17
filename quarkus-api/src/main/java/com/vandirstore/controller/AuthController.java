package com.vandirstore.controller;

import com.vandirstore.dto.AuthRequestDTO;
import com.vandirstore.dto.AuthResponseDTO;
import com.vandirstore.model.User;
import com.vandirstore.service.IUserService;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Duration;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    IUserService userService;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(AuthRequestDTO request) {
        // Correctly validates both email and password against the database
        User user = userService.validateCredentials(request.getEmail(), request.getPassword());
        
        if (user != null && user.getStatus().name().equals("ACTIVE")) {
            String token = Jwt.issuer("https://vandir.com/issuer")
                    .upn(user.getEmail())
                    .groups(user.getRole().getDbValue())
                    .claim("userId", user.getId())
                    .expiresIn(Duration.ofHours(8))
                    .sign();
            
            return Response.ok(new AuthResponseDTO(token, user.getRole().getDbValue())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials or blocked user").build();
    }
}
