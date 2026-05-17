package com.vandirstore.controller;

import com.vandirstore.dto.ProviderDTO;
import com.vandirstore.service.IProviderService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/providers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"Administrador", "Gerente"})
public class ProviderController {

    @Inject
    IProviderService providerService;

    @GET
    public Response listProviders() {
        return Response.ok(providerService.listAllProviders()).build();
    }

    @POST
    public Response createProvider(ProviderDTO request) {
        ProviderDTO created = providerService.createProvider(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public Response getProviderById(@PathParam("id") Integer id) {
        ProviderDTO provider = providerService.findById(id);
        if (provider != null) {
            return Response.ok(provider).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateProvider(@PathParam("id") Integer id, ProviderDTO request) {
        ProviderDTO updated = providerService.updateProvider(id, request);
        if (updated != null) {
            return Response.ok(updated).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProvider(@PathParam("id") Integer id) {
        boolean success = providerService.deleteProvider(id);
        if (success) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
