package priv.ilyan.controller;

import java.io.IOException;
import java.nio.file.Files;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import lombok.extern.slf4j.Slf4j;
import priv.ilyan.model.ResourceResponse;
import priv.ilyan.service.CsvReader;
import priv.ilyan.service.SpreadSheetReader;

@Slf4j
@Path("/resource")
public class ResourceController {

    @Inject
    CsvReader csvReader;

    @Inject
    SpreadSheetReader spreadSheetReader;


    @GET
    @Path("/free-access")
    @Produces(MediaType.APPLICATION_JSON)
    public Response freeAccess() {
        return Response.ok(new ResourceResponse("Content for everyone")).build();
    }

    @RolesAllowed("USER")
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response user() {
        return Response.ok(new ResourceResponse("Content for user")).build();
    }

    @RolesAllowed("ADMIN")
    @GET
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response admin() {
        return Response.ok(new ResourceResponse("Content for admin")).build();
    }

    @RolesAllowed({ "USER", "ADMIN" })
    @POST
    @Path("/upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@MultipartForm FileUploadInput input) throws IOException {
        log.info("File name > " + input.file.fileName());

        if(input.file.fileName().endsWith(".csv")){
            csvReader.readAndPrepare(Files.readString(input.file.uploadedFile()));
        } else if (input.file.fileName().endsWith(".xlsx")) {
            spreadSheetReader.readAndPrepare(input.file.filePath().toFile());
        }
        return Response.ok(new ResourceResponse("Content for user or admin")).build();
    }

    public static class FileUploadInput{
        @FormParam("file")
        public FileUpload file;
    }
}
