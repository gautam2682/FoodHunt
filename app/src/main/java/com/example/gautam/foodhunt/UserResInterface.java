package com.example.gautam.foodhunt;

import com.example.gautam.foodhunt.Modal.ProductResponse;
import com.example.gautam.foodhunt.Modal.ServerRequest;
import com.example.gautam.foodhunt.Modal.ServerResponse;
import com.example.gautam.foodhunt.Modal.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gautam on 20/12/16.
 */

public interface UserResInterface {

    @POST("gauti/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}
