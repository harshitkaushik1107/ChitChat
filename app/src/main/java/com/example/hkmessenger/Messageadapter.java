package com.example.hkmessenger;

import static com.example.hkmessenger.chatWin.reciverImg;
import static com.example.hkmessenger.chatWin.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import static android.view.View.VISIBLE;

import de.hdodenhof.circleimageview.CircleImageView;


public class Messageadapter extends RecyclerView.Adapter {
    Context context;
    ImageView imageview;
    ArrayList<msgModle> messagesAdapter;
    int ITEM_SEND =1;
    int ITEM_RECIVE =2;

    public Messageadapter(Context context, ArrayList<msgModle> messagesAdapter) {
        this.context = context;
        this.messagesAdapter = messagesAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout ,parent,false);
            return new senderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModle msg = messagesAdapter.get(position);
//
        if(holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            if(msg.getMessage().equals("photo")){
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.msgtxt.setVisibility(View.GONE);
                Glide.with(context).load(msg.getImageUri()).into(viewHolder.imageview);
//                Picasso.get().load(msg.getImageUri()).into(viewHolder.imageview);
            }
            viewHolder.msgtxt.setText(msg.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        }
        else {

            if(holder.getClass()==reciverViewHolder.class) {
                reciverViewHolder reciverViewHolder = (reciverViewHolder) holder;
                if (msg.getMessage().equals("photo")) {
                    reciverViewHolder.imageview.setVisibility(View.VISIBLE);
                    reciverViewHolder.msgtxt.setVisibility(View.GONE);
                    Glide.with(context).load(msg.getImageUri()).into(reciverViewHolder.imageview);
//                Picasso.get().load(msg.getImageUri()).into(viewHolder.imageview);
                }
                reciverViewHolder.msgtxt.setText(msg.getMessage());
                Picasso.get().load(reciverImg).into(reciverViewHolder.circleImageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messagesAdapter.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModle msg = messagesAdapter.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(msg.getSenderId())){
            return ITEM_SEND;
        }
        else {
            return  ITEM_RECIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        ImageView imageview;
        TextView msgtxt;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
            imageview = itemView.findViewById(R.id.imagebtn);
        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        ImageView imageview;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);
            imageview = itemView.findViewById(R.id.imagebtn);

        }
    }

}
