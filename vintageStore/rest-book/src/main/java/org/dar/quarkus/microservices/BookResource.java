package org.dar.quarkus.microservices;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;

@Path("/api/books")
public class BookResource {

    @RestClient
    NumberProxy proxy;

    @Inject
    Logger logger;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(
            summary = "Creates a book",
            description = "Creates a Book with an ISBN number"
    )
    @Fallback(fallbackMethod = "fallbackOnCreatingABook")
    @Retry(
            maxRetries = 3,
            delay = 3000
    )
    public Uni<Response> createABook(@FormParam("title") String title,
                                     @FormParam("author") String author,
                                     @FormParam("year") int yearOfPublication,
                                     @FormParam("genre") String genre) {
        return Uni.createFrom().item(() -> {
                    Book book = new Book();
                    book.setIsbn13(proxy.generateIsbnNumbers().toString());
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setYearOfPublication(yearOfPublication);
                    book.setGenre(genre);
                    return book;
                }).onItem().transformToUni(book -> saveBookOnDisk(book)
                        .onItem().transform(savedBook -> {
                            logger.warn("Book saved on disk: " + savedBook);
                            return Response.status(201).entity(savedBook).build();
                        }))
                .onFailure().recoverWithItem(throwable -> {
                    logger.error("Failed to save book on disk", throwable);
                    return Response.status(500).entity("Book could not be saved").build();
                });

    }


    public Uni<Response> fallbackOnCreatingABook(@FormParam("title") String title,
                                                 @FormParam("author") String author,
                                                 @FormParam("year") int yearOfPublication,
                                                 @FormParam("genre") String genre) {

        return Uni.createFrom().item(() -> {
            Book book = new Book();
            book.setIsbn13(proxy.generateIsbnNumbers().toString());
            book.setTitle(title);
            book.setAuthor(author);
            book.setYearOfPublication(yearOfPublication);
            book.setGenre(genre);
            return book;
        }).onItem().transformToUni(book -> saveBookOnDisk(book)
                .onItem().transform(savedBook -> {
                    logger.warn("Book saved on disk: " + savedBook);
                    return Response.status(206).entity(savedBook).build();
                }));
    }

    private Uni<Book> saveBookOnDisk(Book book) {
        return Uni.createFrom().item(() -> {
            String bookJson = JsonbBuilder.create().toJson(book);
            try (PrintWriter out = new PrintWriter("book-" + Instant.now().toEpochMilli() + ".json")) {
                out.println(bookJson);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return book;
        });
    }

}

