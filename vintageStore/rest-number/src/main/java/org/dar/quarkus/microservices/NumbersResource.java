package org.dar.quarkus.microservices;


import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.Random;


@Path("/api/numbers")
public class NumbersResource {
    @Inject
    Logger logger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<IsbnNumbers> generateIsbnNumbers() {
        return Uni.createFrom().item(() -> {
            IsbnNumbers isbn = new IsbnNumbers();
            isbn.setIsbn13("13-" + new Random().nextInt(100_000_000));
            isbn.setIsbn10("10-" + new Random().nextInt(100_000));
            isbn.generationDate = Instant.now();
            logger.info("Numbers generated " + isbn);
            return isbn;
        });
    }


}
