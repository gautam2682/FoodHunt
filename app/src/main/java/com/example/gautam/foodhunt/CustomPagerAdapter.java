package com.example.gautam.foodhunt;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Created by gautam on 14/12/16.
 */

public class CustomPagerAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater inflater;
   int[] mResourses={
           R.drawable.burger,
           R.drawable.pizza,
           R.drawable.fruit
   };

    public  CustomPagerAdapter(Context context){
        mContext=context;
        inflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mResourses.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemview=inflater.inflate(R.layout.pageritem,container,false);
        ImageView imageView=(ImageView)itemview.findViewById(R.id.pagerimageView);
        imageView.setImageResource(mResourses[position]);
        container.addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
