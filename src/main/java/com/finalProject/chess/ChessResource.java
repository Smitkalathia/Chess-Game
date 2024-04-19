package com.finalProject.chess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/resources")
public class ChessResource {
    @GET
    @Path("/{imageName}")
    @Produces("image/png")
    public Response getImage(@PathParam("imageName") String imageName) throws IOException {
        Path imagePath = (Path) Paths.get("src", "main", "resources", imageName + ".png");

        if (Files.exists((java.nio.file.Path) imagePath)) {
            byte[] imageData = Files.readAllBytes((java.nio.file.Path) imagePath);
            return Response.ok(imageData).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Image not found").build();
        }
    }
}
