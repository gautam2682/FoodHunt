package in.OrderCruch;

;

import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gautam on 20/12/16.
 */

public interface UserResInterface {

    @POST("odercrunch_android/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}
