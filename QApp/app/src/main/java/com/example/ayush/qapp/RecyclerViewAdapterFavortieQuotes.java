package com.example.ayush.qapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class RecyclerViewAdapterFavortieQuotes extends RecyclerView.Adapter<RecyclerViewAdapterFavortieQuotes.RecyclerViewHolderFavorite> {
    int len;
    LinkedList<String> data;
    Context context;
    int lastPosition=-1;
    public RecyclerViewAdapterFavortieQuotes(LinkedList<String> data, Context context){
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerViewHolderFavorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_favoritequotes,parent,false);
        return new RecyclerViewAdapterFavortieQuotes.RecyclerViewHolderFavorite(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolderFavorite holder, int position) {
        final int q =position;
        holder.textView.setText(data.get(position));
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,data.get(q));
                intent.setType("text/plain");
                context.startActivity(Intent.createChooser(intent,"Share this Quote with"));
            }
        });

       holder.deleteButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                deleteItem(holder.getAdapterPosition());
           }
       });
        if(position >lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.recycelerview_animation);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Copied Quote",data.get(q));
                assert clipboardManager!=null;
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context,"Copied to Clipboard",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    void deleteItem(int index) {
        data.remove(index);
        notifyItemRemoved(index);
        if(data.isEmpty()){
            Favorite_Quotes.mExploreButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerViewHolderFavorite extends RecyclerView.ViewHolder{
        ImageButton shareButton;
        ImageButton deleteButton;
        TextView textView;
        public RecyclerViewHolderFavorite(View itemView) {
            super(itemView);
            shareButton = itemView.findViewById(R.id.shareButton);
            textView = itemView.findViewById(R.id.textView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
