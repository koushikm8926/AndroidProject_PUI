package com.example.newspaper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleView extends RecyclerView.ViewHolder {


    private ImageView articleImage;
    private TextView articleTitle;
    private TextView articleDescription;


    public ArticleView(@NonNull View itemView) {
        super(itemView);
        articleImage = itemView.findViewById(R.id.articleImage);
        articleTitle = itemView.findViewById(R.id.articleTitle);
        articleDescription = itemView.findViewById(R.id.articleDescription);
    }



    public ImageView getArticleImage() {
        return articleImage;
    }

    public TextView getArticleTitle() {
        return articleTitle;
    }

    public TextView getArticleDescription() {
        return articleDescription;
    }


}
