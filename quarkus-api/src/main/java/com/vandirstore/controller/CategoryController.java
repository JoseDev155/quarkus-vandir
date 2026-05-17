package com.vandirstore.controller;

import com.vandirstore.dto.CategoryDTO;
import com.vandirstore.service.ICategoryService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryController {

    @Inject
    ICategoryService categoryService;

    @GET
    @RolesAllowed({"Administrador", "Gerente", "Vendedor"})
    public Response listCategories() {
        return Response.ok(categoryService.listAllCategories()).build();
    }

    @POST
    @RolesAllowed({"Administrador", "Gerente"})
    public Response createCategory(CategoryDTO request) {
        CategoryDTO created = categoryService.createCategory(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Administrador", "Gerente", "Vendedor"})
    public Response getCategoryById(@PathParam("id") Integer id) {
        CategoryDTO category = categoryService.findById(id);
        if (category != null) {
            return Response.ok(category).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Administrador", "Gerente"})
    public Response updateCategory(@PathParam("id") Integer id, CategoryDTO request) {
        CategoryDTO updated = categoryService.updateCategory(id, request);
        if (updated != null) {
            return Response.ok(updated).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Administrador", "Gerente"})
    public Response deleteCategory(@PathParam("id") Integer id) {
        boolean success = categoryService.deleteCategory(id);
        if (success) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
