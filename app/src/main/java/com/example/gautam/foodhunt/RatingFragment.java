package com.example.gautam.foodhunt;

import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

/**
 * Created by gautam on 20/6/17.
 */

public class RatingFragment extends Fragment {
    Button submit_rating;
    String p_id;
    RatingBar ratingBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.rating_fragment,container,false);
        intiViews(view);
        p_id=getArguments().getString(Constants.PID);

        submit_ratinglistener();;
        return view;
    }
    public static RatingFragment newInstace(String p_id){
        RatingFragment fragment=new RatingFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constants.PID,p_id);
        fragment.setArguments(bundle);
        return fragment;

    }


    private void intiViews(View view) {
        submit_rating=(Button)view.findViewById(R.id.submit_ratings);
        ratingBar=(RatingBar)view.findViewById(R.id.rating_bar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
       // stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
       // ratingBar.setVisibility(View.INVISIBLE);

    }

    private void submit_ratinglistener() {
        submit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentFragment fragment;
                fragment = CommentFragment.newInstace(p_id,true, " ");;
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_control,fragment);

                ft.commit();

            }
        });
    }

}
