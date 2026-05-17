package com.vandirstore.controller;

import com.vandirstore.dto.ConfigurationDTO;
import com.vandirstore.service.ISystemConfigService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/config")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("Administrador")
public class SystemConfigController {

    @Inject
    ISystemConfigService configService;

    @GET
    public Response getConfig() {
        ConfigurationDTO config = configService.getGlobalConfig();
        if (config != null) {
            return Response.ok(config).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    public Response updateConfig(ConfigurationDTO request) {
        ConfigurationDTO updated = configService.updateGlobalConfig(request);
        if (updated != null) {
            return Response.ok(updated).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/backups")
    public Response listBackups() {
        return Response.ok(configService.listBackupHistory()).build();
    }
}
