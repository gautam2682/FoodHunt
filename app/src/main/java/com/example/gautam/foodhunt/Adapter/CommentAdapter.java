package com.example.gautam.foodhunt.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.gautam.foodhunt.Modal.User;
import com.example.gautam.foodhunt.R;

import java.util.ArrayList;

/**
 * Created by gautam on 19/12/16.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<User> users;
    Context context;
    editcomment listener;

    public CommentAdapter(ArrayList<User> users, Context context,editcomment listener) {

        this.users = users;
        this.context = context;
        this.listener=listener;
    }


    public interface editcomment{
        public void editcommentlistener( int pos,String comm);
        public void deletecommentlistener(int pos);
    }

    public void swap(ArrayList<User> newusers){
        if(users!=null){
            users.clear();
            users.addAll(users);
        }else {
            users=newusers;
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.user_no.setText(users.get(position).getEmail());
        holder.comment_text.setText(users.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView comment_text,user_no;
        public ViewHolder(View itemView) {
            super(itemView);
            comment_text=(TextView)itemView.findViewById(R.id.comment_text);
            user_no=(TextView)itemView.findViewById(R.id.user_no);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            final int pos=getAdapterPosition();
            Context context=view.getContext();
            MaterialDialog materialDialog=new MaterialDialog.Builder(context)
                    .customView(R.layout.prompts,false)
                    .positiveText("Submit")
                    .positiveColor(context.getResources().getColor(R.color.light_blue500))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            EditText edit=(EditText)dialog.getCustomView().findViewById(R.id.editTextDialogUserInput);
                            String comm=edit.getText().toString();
                         listener.editcommentlistener(pos,comm);

                        }
                    })
                    .negativeText("Delete")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            listener.deletecommentlistener(pos);

                        }
                    })
                    .neutralText("Cancel")
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
                    .neutralColor(view.getResources().getColor(R.color.colorPrimaryDark))
                    .title("Edit your comment")
                    .build();
            materialDialog.show();
        }
    }


}
