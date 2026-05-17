package com.vandirstore.controller;

import com.vandirstore.dto.ProductRequestDTO;
import com.vandirstore.dto.ProductResponseDTO;
import com.vandirstore.service.IProductService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    IProductService productService;

    @GET
    @RolesAllowed({"Administrador", "Gerente", "Vendedor"})
    public Response listProducts() {
        return Response.ok(productService.listAllProducts()).build();
    }

    @GET
    @Path("/low-stock")
    @RolesAllowed({"Administrador", "Gerente"})
    public Response listLowStock() {
        return Response.ok(productService.listLowStock()).build();
    }

    @POST
    @RolesAllowed({"Administrador", "Gerente"})
    public Response createProduct(ProductRequestDTO request) {
        ProductResponseDTO created = productService.createProduct(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Administrador", "Gerente", "Vendedor"})
    public Response getProductById(@PathParam("id") Integer id) {
        ProductResponseDTO product = productService.findById(id);
        if (product != null) {
            return Response.ok(product).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Administrador", "Gerente"})
    public Response updateProduct(@PathParam("id") Integer id, ProductRequestDTO request) {
        ProductResponseDTO updated = productService.updateProduct(id, request);
        if (updated != null) {
            return Response.ok(updated).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Administrador", "Gerente"})
    public Response deleteProduct(@PathParam("id") Integer id) {
        boolean success = productService.deleteProduct(id);
        if (success) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
