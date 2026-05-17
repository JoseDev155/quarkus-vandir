package com.vandirstore.controller;

import com.vandirstore.dto.SaleRequestDTO;
import com.vandirstore.dto.SaleResponseDTO;
import com.vandirstore.service.ISaleService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/sales")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SaleController {

    @Inject
    ISaleService saleService;

    @GET
    @RolesAllowed({"Administrador", "Gerente"})
    public Response listSales() {
        return Response.ok(saleService.listAllSales()).build();
    }

    @POST
    @RolesAllowed({"Administrador", "Gerente", "Vendedor"})
    public Response createSale(SaleRequestDTO request) {
        try {
            SaleResponseDTO created = saleService.createSale(request);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Administrador", "Gerente", "Vendedor"})
    public Response getSaleById(@PathParam("id") Integer id) {
        SaleResponseDTO sale = saleService.findById(id);
        if (sale != null) {
            return Response.ok(sale).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}/cancel")
    @RolesAllowed({"Administrador", "Gerente"})
    public Response cancelSale(@PathParam("id") Integer id) {
        boolean success = saleService.cancelSale(id);
        if (success) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Administrador")
    public Response deleteSale(@PathParam("id") Integer id) {
        boolean success = saleService.deleteSale(id);
        if (success) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
