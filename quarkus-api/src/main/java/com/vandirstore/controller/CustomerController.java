package com.vandirstore.controller;

import com.vandirstore.dto.CustomerDTO;
import com.vandirstore.service.ICustomerService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"Administrador", "Gerente", "Vendedor"})
public class CustomerController {

    @Inject
    ICustomerService customerService;

    @GET
    public Response listCustomers() {
        return Response.ok(customerService.listAllCustomers()).build();
    }

    @POST
    public Response createCustomer(CustomerDTO request) {
        CustomerDTO created = customerService.createCustomer(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Integer id) {
        CustomerDTO customer = customerService.findById(id);
        if (customer != null) {
            return Response.ok(customer).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Integer id, CustomerDTO request) {
        CustomerDTO updated = customerService.updateCustomer(id, request);
        if (updated != null) {
            return Response.ok(updated).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Integer id) {
        boolean success = customerService.deleteCustomer(id);
        if (success) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
