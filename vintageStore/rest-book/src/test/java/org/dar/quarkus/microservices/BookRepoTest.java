package org.dar.quarkus.microservices;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;

@QuarkusTest
public class BookRepoTest {
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        bookRepository = new BookRepository();
        // Add some books to the repository for testing
        var book = new Book();
        book.setIsbn13("13-123455");
        book.setAuthor("Test Author");
        book.setTitle("Test Book");
        book.setYearOfPublication(2000);
        bookRepository.getBookrepo().put("13-123455", new Book());
    }

//    @Test
//    public void testFindBook() {
//        Uni<Book> bookUni = bookRepository.findBook("13-123455");
//
//        // Subscribe and assert the result
//        ((Uni<?>) bookUni).subscribe().with(book -> {
//            assertEquals("Test Book", book.);
//            assertEquals("Test Author", book.getAuthor());
//        });
//    }

}

