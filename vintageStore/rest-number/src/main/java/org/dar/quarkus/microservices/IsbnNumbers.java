package org.dar.quarkus.microservices;

import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import java.time.Instant;

@Getter
@Setter
public class IsbnNumbers {
    @JsonbProperty("isbn_10")
    public String isbn10;
    @JsonbProperty("isbn_13")
    public String isbn13;
    @JsonbTransient
    public Instant generationDate;


    @Override
    public String toString() {
        return "IsbnNumbers{" +
               "isbn_10='" + isbn10 + '\'' +
               ", isbn_13='" + isbn13 + '\'' +
               ", generationDate=" + generationDate +
               '}';
    }
}