package in.OrderCruch.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.OrderCruch.ActProductInfo;
import in.OrderCruch.Modal.ProductVersion;
import in.OrderCruch.R;

/**
 * Created by gautam on 19/2/17.
 */

public class Carousel_Adapter extends RecyclerView.Adapter<Carousel_Adapter.ViewHolder> {

    private List<ProductVersion> mainModels;
    private Context context;
    private OnClicklisteners listener;



    public Carousel_Adapter(List<ProductVersion> mainModels, Context context,OnClicklisteners listener) {
        this.mainModels = mainModels;
        this.context = context;
        this.listener=listener;

    }



    public interface OnClicklisteners{
        public void onPosClicked(int pos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.main_text.setText(mainModels.get(position).getP_name());

        Picasso.with(context).load(mainModels.get(position).getP_image()).fit().centerCrop()
                .into(holder.mainimage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) holder.mainimage.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {

                                   // int mutedDarkColor = palette.getDarkMutedColor(context.getResources().getColor(R.color.colorPrimaryDark));
                                    int vibrantColor = palette.getVibrantColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorAccent));

                                    //int color=palette.getLightVibrantColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorAccent));
                                    holder.titleBackground.setBackgroundColor(vibrantColor);
                                    holder.titleBackground.getBackground().setAlpha(200);


                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });


    }



    @Override
    public int getItemCount() {
        return mainModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.main_image)
        ImageView mainimage;
        @BindView(R.id.main_text)
        TextView main_text;
        @BindView(R.id.title_background)
        View titleBackground;

        Typeface face;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
         //   face= Typeface.createFromAsset(itemView.getContext().getAssets(), "BreeSerif-Regular.ttf");
           // main_text.setTypeface(face);

        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
           // listener.onPosClicked(pos);
            Intent intent =new Intent(context,ActProductInfo.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("DATAINTENT",mainModels.get(pos).getP_id());
            context.startActivity(intent);



        }
    }
}
