package org.dar.quarkus.microservices;


import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@RegisterRestClient(configKey = "number.proxy")
@Path("api/numbers")
@ApplicationScoped
public interface NumberProxy {

@GET
@Produces(MediaType.APPLICATION_JSON)
Uni<IsbnThirteen> generateIsbnNumbers();
}
