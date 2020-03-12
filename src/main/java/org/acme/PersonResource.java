package org.acme;

import org.acme.models.Person;
import org.acme.services.PersonService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    private static final Logger LOGGER = Logger.getLogger(PersonResource.class);

    @Inject
    PersonService service;

    @Operation(summary = "Returns all the person from the database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Person.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No Person")
    @GET
    public Response getAllPerson(){
        List<Person> persons = service.findAllPerson();
        return Response.ok(persons).build();
    }

    @Operation(summary = "Returns a person for a given identifier")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Person.class)))
    @APIResponse(responseCode = "204", description = "The person is not found for a given identifier")
    @GET
    @Path("/{id}")
    public Response getPerson(
            @Parameter(description = "Person identifier", required = true)
            @PathParam("id") Long id){
        Person person = service.findPersonById(id);
        if (person != null) {
            return Response.ok(person).build();
        } else {
            return Response.noContent().build();
        }
    }

    @Operation(summary = "Creates a valid person")
    @APIResponse(responseCode = "201", description = "The URI of the created person",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @POST
    @Transactional
    @Path("/add")
    public Response createPerson(
            @RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Person.class)))
            @Valid Person person, @Context UriInfo uriInfo) {
        person = service.persistPerson(person);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(person.id));
        return Response.ok(person).status(201).build();
    }

    @Operation(summary = "Updates an exiting  person")
    @APIResponse(responseCode = "200", description = "The updated person",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Person.class)))
    @PUT
    public Response updatePerson(
            @RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Person.class)))
            @Valid Person person){
        person = service.updatePerson(person);
        return Response.ok(person).build();
    }

    @Operation(summary = "Deletes an exiting person")
    @APIResponse(responseCode = "204")
    @DELETE
    @Path("/{id}")
    public Response deletePerson(
            @Parameter(description = "Person identifier", required = true)
            @PathParam("id") Long id){
        service.deletePerson(id);
        return Response.noContent().build();
    }
}
