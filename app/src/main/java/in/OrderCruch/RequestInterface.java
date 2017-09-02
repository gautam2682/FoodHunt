package in.OrderCruch;

import in.OrderCruch.Modal.ProductResponse;
import in.OrderCruch.Modal.ServerRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by gautam on 14/12/16.
 */

public interface RequestInterface {

    @GET("odercrunch_android/")
    Call<ProductResponse> getProducts();

    @POST("odercrunch_android/")
    Call<ProductResponse> operation(@Body ServerRequest request);


}
