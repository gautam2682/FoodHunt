package in.OrderCruch;

import in.OrderCruch.Modal.ProductResponse;
import in.OrderCruch.Modal.ServerRequest;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by gautam on 27/12/16.
 */

public interface RxjavaInterface {

    @POST("odercrunch_android/")
    Observable<ProductResponse> operation(@Body ServerRequest request);


    @GET("odercrunch_android/")
    Observable<ProductResponse> getProducts();
}
