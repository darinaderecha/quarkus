package org.dar.quarkus.microservices;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.awt.*;

@RegisterRestClient(configKey = "number.proxy")
@Path("api/numbers")
public interface NumberProxy {
@GET
@Produces(MediaType.APPLICATION_JSON)
IsbnThirteen generateIsbnNumbers();
}
