package com.example.gautam.foodhunt.Modal;

/**
 * Created by gautam on 9/6/16.
 */
public class ServerRequest {
    private String operation;
    private String query;
    private User user;


    public void setOperation(String operation) {
        this.operation = operation;
    }



    public void setquery(String query){
        this.query=query;
    }

    public void setUser(User user){
        this.user=user;
    }
}
