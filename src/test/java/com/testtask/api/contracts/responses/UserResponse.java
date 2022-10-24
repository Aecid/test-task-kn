package com.testtask.api.contracts.responses;

import com.testtask.api.contracts.dataObjects.BookDTO;

import java.util.List;

public class UserResponse {
    public String userId;
    public String username;
    public List<BookDTO> books;
}
