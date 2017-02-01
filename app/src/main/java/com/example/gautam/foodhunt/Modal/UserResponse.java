package com.example.gautam.foodhunt.Modal;

import retrofit2.Call;

/**
 * Created by gautam on 19/12/16.
 */

public class UserResponse {
    private User[] user;
    private String result;

    public String getResult() {
        return result;
    }

    public User[] getUsers(){return user;}

}
