package com.example.gautam.foodhunt;

import com.example.gautam.foodhunt.Modal.ProductResponse;
import com.example.gautam.foodhunt.Modal.ServerRequest;
import com.example.gautam.foodhunt.Modal.User;
import com.example.gautam.foodhunt.Modal.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by gautam on 14/12/16.
 */

public interface RequestInterface {

    @GET("gauti/")
    Call<ProductResponse> getProducts();

    @POST("gauti/")
    Call<ProductResponse> operation(@Body ServerRequest request);


}
