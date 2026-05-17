package com.vandirstore.controller;

import com.vandirstore.dto.PurchaseOrderRequestDTO;
import com.vandirstore.dto.PurchaseOrderResponseDTO;
import com.vandirstore.model.enums.PurchaseOrderStatus;
import com.vandirstore.service.IPurchaseOrderService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"Administrador", "Gerente"})
public class PurchaseOrderController {

    @Inject
    IPurchaseOrderService orderService;

    @GET
    public Response listOrders() {
        return Response.ok(orderService.listAllOrders()).build();
    }

    @POST
    public Response createOrder(PurchaseOrderRequestDTO request) {
        try {
            PurchaseOrderResponseDTO created = orderService.createOrder(request);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") Integer id) {
        PurchaseOrderResponseDTO order = orderService.findById(id);
        if (order != null) {
            return Response.ok(order).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PATCH
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") Integer id, @QueryParam("status") PurchaseOrderStatus status) {
        PurchaseOrderResponseDTO updated = orderService.updateOrderStatus(id, status);
        if (updated != null) {
            return Response.ok(updated).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Administrador")
    public Response deleteOrder(@PathParam("id") Integer id) {
        boolean success = orderService.deleteOrder(id);
        if (success) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
