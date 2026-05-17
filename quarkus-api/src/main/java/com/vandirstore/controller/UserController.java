package com.vandirstore.controller;

import com.vandirstore.dto.UserRequestDTO;
import com.vandirstore.dto.UserResponseDTO;
import com.vandirstore.service.IUserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("Administrador")
public class UserController {

    @Inject
    IUserService userService;

    @GET
    public Response listUsers() {
        return Response.ok(userService.listAllUsers()).build();
    }

    @POST
    public Response createUser(UserRequestDTO request) {
        UserResponseDTO created = userService.createUser(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Integer id) {
        UserResponseDTO user = userService.findById(id);
        if (user != null) {
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Integer id, UserRequestDTO request) {
        UserResponseDTO updated = userService.updateUser(id, request);
        if (updated != null) {
            return Response.ok(updated).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PATCH
    @Path("/{id}/status")
    public Response changeStatus(@PathParam("id") Integer id, @QueryParam("active") boolean active) {
        boolean success = userService.changeStatus(id, active);
        if (success) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Integer id) {
        boolean success = userService.deleteUser(id);
        if (success) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
