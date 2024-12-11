/*
This adapter will be responsible for:

Populating the RecyclerView with NewsArticle objects
Binding the data to the CardView layout
Handling item clicks (if desired)
 */

package com.example.newspaper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<ArticleView> {
    private List<Article> articles;
    private MainActivity ma;

    public NewsAdapter(MainActivity ma, List<Article> articles) {
        this.ma = ma;

        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view, parent, false);
        ArticleView card = new ArticleView(cardView);
        return card;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleView card, int position) {
        Article article = articles.get(position);

        if(article.getTitle()!=null) {
            card.getArticleTitle().setText(Utils.insertHtmlText(article.getTitle()));
        }


        if(article.getAbstractText()!=null) {
            card.getArticleDescription().setText(Utils.insertHtmlText(article.getAbstractText()));
        }

        if (article.getImage() !=null){
            String image_thumbnail = article.getImage().getImage();
            if(image_thumbnail!=null && !image_thumbnail.isEmpty()) {
                Bitmap image = Utils.base64StringToImg(image_thumbnail);
                if(image!=null) {
                    card.getArticleImage().setImageBitmap(image);
                }
            }
        }
        else{
            card.getArticleImage().setImageBitmap(BitmapFactory.decodeResource(ma.getResources(), R.drawable.picture_placeholder));
        }

        card.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArticleDetails.class);
                intent.putExtra("article_id", article.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    public void updateData(List<Article> newArticles) {
        this.articles = newArticles;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

}
