package org.dar.quarkus.microservices;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/*
Mock book repository
Do not pay attention at design at all
I made it to simplify and for avoiding using db in that project
testdata in test package - testdata.sh
 */
@Getter
public class BookRepository {

    private HashMap<String, Book> bookrepo;

    public BookRepository() {
        this.bookrepo = new HashMap<>();
    }

    public Uni<Book> findBook(String isbn) {
        return Uni.createFrom().item(bookrepo.get(isbn));
    }

    public Uni<Book> findBookByTitle(String title) {
        return Uni.createFrom().item(() -> {
            for (Map.Entry<String, Book> entry : bookrepo.entrySet()) {
                if (entry.getValue().getTitle().equals(title)) {
                    return entry.getValue();
                }
            }
            return null;
        });


    }
    public Multi<Book> findBookGenre(String title) {
    }
}
