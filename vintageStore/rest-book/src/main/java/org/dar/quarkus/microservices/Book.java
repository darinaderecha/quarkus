package org.dar.quarkus.microservices;


import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;
@Getter
@Setter
@Schema(description = "This is a book")
    public class Book {

        @JsonbProperty("isbn_13")
        @Schema(required = true)
        private String isbn13;
        @Schema(required = true)
        private String title;
        private String author;
        @JsonbProperty("year_of_publication")
        private int yearOfPublication;
        private String genre;
        @JsonbDateFormat("yyyy/MM/dd")
        @JsonbProperty("creation_date")
        @Schema(implementation = String.class, format = "date")
        private Instant creationDate;



    @Override
    public String toString() {
        return "Book{" +
               "isbn_13='" + isbn13 + '\'' +
               ", title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", yearOfPublication=" + yearOfPublication +
               ", genre='" + genre + '\'' +
               ", creationDate=" + creationDate +
               '}';
    }
}

