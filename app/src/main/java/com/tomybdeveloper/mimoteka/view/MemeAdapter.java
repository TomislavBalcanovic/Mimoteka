package com.tomybdeveloper.mimoteka.view;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tomybdeveloper.mimoteka.R;
import com.tomybdeveloper.mimoteka.model.Meme;

import java.util.List;

public class MemeAdapter extends RecyclerView.Adapter<MemeAdapter.ViewHolder> {

    private Context context;
    private List<Meme> memeList;

    public MemeAdapter(Context context, List<Meme> memeList) {
        this.context = context;
        this.memeList = memeList;
    }

    @NonNull
    @Override
    public MemeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meme_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MemeAdapter.ViewHolder holder, int position) {

        Meme meme = memeList.get(position);
        String imageUrl;


        holder.textViewUser.setText(meme.getUserName());
        holder.textViewTitle.setText(meme.getTitle());
        holder.textViewDescription.setText(meme.getDescription());
        imageUrl = meme.getImageUrl();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(meme.getTimeAdded().getSeconds() * 1000);  // adding time
        holder.dateAdded.setText(timeAgo);


        Glide.with(context).load(imageUrl).centerCrop().placeholder(R.drawable.image_three).into(holder.imageView);


    }

    @Override
    public int getItemCount() {

        return memeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView textViewUser;
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final TextView dateAdded;
//        String userName;
//        String userId;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            imageView = itemView.findViewById(R.id.imageViewRow);
            textViewUser = itemView.findViewById(R.id.textViewUserIdName);
            textViewTitle = itemView.findViewById(R.id.textViewTitleRow);
            textViewDescription = itemView.findViewById(R.id.textViewDescriptionRow);
            dateAdded = itemView.findViewById(R.id.textViewDateRow);


        }
    }
}
