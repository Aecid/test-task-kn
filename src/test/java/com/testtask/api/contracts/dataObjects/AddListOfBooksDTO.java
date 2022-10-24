package com.testtask.api.contracts.dataObjects;

import java.util.ArrayList;
import java.util.List;

public class AddListOfBooksDTO {
    public String userId;
    public List<IsbnDTO> collectionOfIsbns;

    public AddListOfBooksDTO(String userId, String... isbnList)
    {
        this.userId = userId;
        List<IsbnDTO> resultList = new ArrayList<>();
        for (String isbn : isbnList) {
            resultList.add(new IsbnDTO(isbn));
        }
        this.collectionOfIsbns = resultList;
    }
}
