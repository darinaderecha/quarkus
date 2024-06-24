package org.dar.quarkus.microservices;

public class MockNumberProxy implements NumberProxy {
    @Override
    public IsbnThirteen generateIsbnNumbers() {
        IsbnThirteen isbt = new IsbnThirteen();
        isbt.isbn13 = "13-mock";
        return isbt;
    }
}
