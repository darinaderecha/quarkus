package org.dar.quarkus.microservices;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;

@QuarkusTest
public class BookRepoTest {
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        bookRepository = new BookRepository();
        var book = new Book();
        book.setIsbn13("13-123455");
        book.setAuthor("Test Author");
        book.setTitle("Test Book");
        book.setYearOfPublication(2000);
        bookRepository.getBookrepo().put("13-123455", new Book());
    }



}

