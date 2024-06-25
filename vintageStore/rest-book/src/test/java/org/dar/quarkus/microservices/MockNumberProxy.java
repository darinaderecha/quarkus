package org.dar.quarkus.microservices;

import io.smallrye.mutiny.Uni;

public class MockNumberProxy implements NumberProxy {
    @Override
    public Uni<IsbnThirteen> generateIsbnNumbers() {
        return Uni.createFrom().item(() -> {
            IsbnThirteen isbn = new IsbnThirteen();
            isbn.isbn13 = "13-mock";
            return isbn;
        });
    }
}
