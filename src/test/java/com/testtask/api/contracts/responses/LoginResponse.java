package com.testtask.api.contracts.responses;

import java.util.Date;

public class LoginResponse {
    public String userId;
    public String username;
    public String password;
    public String token;
    public Date expires;
    public Date created_date;
    public Boolean isActive;
}
