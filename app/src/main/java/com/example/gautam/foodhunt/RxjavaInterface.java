package com.example.gautam.foodhunt;

import com.example.gautam.foodhunt.Modal.ProductResponse;
import com.example.gautam.foodhunt.Modal.ServerRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by gautam on 27/12/16.
 */

public interface RxjavaInterface {

    @POST("gauti/")
    Observable<ProductResponse> operation(@Body ServerRequest request);


    @GET("gauti/")
    Observable<ProductResponse> getProducts();
}
