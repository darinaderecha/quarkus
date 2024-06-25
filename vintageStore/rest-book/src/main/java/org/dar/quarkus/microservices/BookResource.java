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

    private final BookRepository br;

    public BookResource() {
        br = new BookRepository();
    }

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
        return proxy.generateIsbnNumbers().map(isbnThirteen -> {
             Book book = new Book();
             book.setIsbn13(isbnThirteen.isbn13);
             book.setTitle(title);
             book.setAuthor(author);
             book.setYearOfPublication(yearOfPublication);
             book.setGenre(genre);
             book.setCreationDate(Instant.now());
             book.setPrice(book.generateBasePrice());
             return book;
         }).chain(book -> saveBookOnDisk(book)
                 .map(savedBook -> {
                            logger.warn("Book saved on disk: " + savedBook);
                            br.getBookrepo().put(savedBook.getIsbn13(), savedBook);
                            return Response.status(201).entity(savedBook).build();
                        }))
                .onFailure().recoverWithItem(throwable -> {
                    logger.error("Failed to save book on disk", throwable);
                    return Response.status(500).entity("Book could not be saved").build();
                });


    }


    @GET
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getTotalPriceByTitle (@PathParam("title") String  title) {
        return br.findBookByTitle(title)
                .onItem().ifNotNull().transform(book -> {
                    double priceWithTaxes = Double.parseDouble(book.getPrice()) * 1.25; // Add taxes
                    book.setPrice(String.valueOf(priceWithTaxes));
                    return Response.status(200).entity("Price for that book is : " + priceWithTaxes).build();
                });

    }

    @GET
    @Path("/like/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> suggestBooks(@PathParam("title") String title) {
                    return br.findBookGenre(title).map(suggestion -> {
                        if( suggestion.isEmpty()) {
                            return Response.status(204)
                                    .entity("There is no books at genre you are looking for. Try for another one" )
                                    .build();
                        } else{
                            return Response.status(200)
                                    .entity("Maybe look for that one? " + suggestion.get(0).getTitle() + " by "
                                    + suggestion.get(0).getAuthor())
                                    .build();
                        }
                    });

    }

    public Uni<Response> fallbackOnCreatingABook(@FormParam("title") String title,
                                                 @FormParam("author") String author,
                                                 @FormParam("year") int yearOfPublication,
                                                 @FormParam("genre") String genre) {

        return proxy.generateIsbnNumbers().map(isbnThirteen -> {
            Book book = new Book();
            book.setIsbn13(isbnThirteen.isbn13);
            book.setTitle(title);
            book.setAuthor(author);
            book.setYearOfPublication(yearOfPublication);
            book.setGenre(genre);
            book.setCreationDate(Instant.now());
            book.setPrice(book.generateBasePrice());
            return book;
        }).flatMap(book -> saveBookOnDisk(book)
                .map(savedBook -> {
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

