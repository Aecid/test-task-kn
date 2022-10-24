package com.testtask.api.contracts.dataObjects;

public class UserDTO {
    public String userName;
    public String password;

    public UserDTO (String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }
}
