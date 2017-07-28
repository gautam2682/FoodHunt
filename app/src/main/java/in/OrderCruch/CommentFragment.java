package in.OrderCruch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import butterknife.BindView;
import butterknife.ButterKnife;
import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.ServerResponse;
import in.OrderCruch.Modal.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gautam on 20/6/17.
 */

public class CommentFragment extends Fragment {
    @BindView(R.id.btn_post)
    Button btn_post;
    @BindView(R.id.addcomment)
    EditText add_comment;
    @BindView(R.id.linear_comment)
    LinearLayout linearcomment;
    @BindView(R.id.textcomment)
    TextView textcomment;

    String p_id;
    Boolean visiblity;
    String comment;
    SharedPreferences pref;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.comment_fragment,container,false);
        ButterKnife.bind(this,view);
        pref=getActivity().getSharedPreferences("ABC", Context.MODE_PRIVATE);

        p_id=getArguments().getString(Constants.PID);
        visiblity=getArguments().getBoolean(Constants.visiblity);
        comment=getArguments().getString(Constants.comment);
        add_comment.setText(comment);


        if(visiblity){
            linearcomment.setVisibility(View.VISIBLE);
        }else {
            linearcomment.setVisibility(View.GONE);
        }

        addComment();
        textcomment.setVisibility(View.GONE);

       add_comment.requestFocus();

        return view;
    }

    public static CommentFragment newInstace(String p_id, Boolean vis, String comment){
        CommentFragment fragment=new CommentFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constants.PID,p_id);
        bundle.putBoolean(Constants.visiblity,vis);
        bundle.putString(Constants.comment,comment);
        fragment.setArguments(bundle);
        return fragment;

    }

    private void addComment() {
        pref=getActivity().getSharedPreferences("ABC", Context.MODE_PRIVATE);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add_comment.getText().toString().isEmpty()) {
                   // Snackbar.make(, "Comment cannot be Empty", Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "Comment cannot be Empty", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("posting your comment");
                    progressDialog.show();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.base_url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    User user = new User();
                    user.setP_id(p_id);
                    user.setComment(add_comment.getText().toString());
                    user.setName(pref.getString(Constants.NAME,""));
                    user.setCan_comment(1);
                    user.setEmail(pref.getString(Constants.EMAIL," "));
                    final ServerRequest request = new ServerRequest();
                    request.setOperation(Constants.addcomment);
                    request.setUser(user);
                    UserResInterface requestInterface = retrofit.create(UserResInterface.class);
                    Call<ServerResponse> response = requestInterface.operation(request);
                    response.enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            progressDialog.dismiss();
                           // Snackbar.make(coordinatorLayout, "Successfully added ", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "Successfully added ", Toast.LENGTH_SHORT).show();
                            add_comment.setText("");
                            //loadcommentresponse(response);
                          linearcomment.setVisibility(View.GONE);
                          //  Intent intent=new Intent(getActivity(),ActProductInfo.class);
                           // startActivity(intent);
                           // getActivity().finish();
                           textcomment.setVisibility(View.VISIBLE);


                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            //SnackbarFailed();

                        }
                    });

                }
            }
        });
    }
}
