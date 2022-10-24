package com.testtask.api.contracts.dataObjects;

public class PutBookDTO {
    public String userId;
    public String isbn;

    public PutBookDTO(String userId, String isbn)
    {
        this.userId = userId;
        this.isbn = isbn;
    }
}
