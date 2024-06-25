package org.dar.quarkus.microservices;

import io.smallrye.mutiny.Uni;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
Mock book repository
Do not pay attention at design at all
I made it to simplify and for avoiding using db in that project
testdata in test package - testdata.sh
 */
@Getter
public class BookRepository {

    private ConcurrentHashMap<String, Book> bookrepo;

    public BookRepository() {
        this.bookrepo = new ConcurrentHashMap<>();
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
    public Uni<List<Book>> findBookGenre(String title) {
        List<Book> suggestions = new ArrayList<>();
        String genre = "";
        for (Map.Entry<String, Book> entry : bookrepo.entrySet()) {
            if (entry.getValue().getTitle().equals(title)) {
                 genre = entry.getValue().getGenre();
            }
        }
        for (Map.Entry<String, Book> entry : bookrepo.entrySet()) {
            if (entry.getValue().getGenre().equalsIgnoreCase(genre) && !entry.getValue().getTitle().equalsIgnoreCase(title)) {
                suggestions.add(entry.getValue());
            }
        }
        return  Uni.createFrom().item(suggestions);
    }
}
