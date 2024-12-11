package com.example.newspaper;


import android.graphics.Bitmap;

import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ArticleDetails extends AppCompatActivity {

    private ImageView detailImage;
    private TextView detailTitle;
    private TextView detailDescription;
    private TextView detailBody;


    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set status bar color (which display the level of battery, the hour, etc ...) to black
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);


        // Import all the view elements
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailDescription = findViewById(R.id.detailDescription);
        detailBody = findViewById(R.id.detailBody);

        // Retrieve the article
        int article_id = (int) getIntent().getExtras().get("article_id");

        // Download articles
        Thread t = new Thread(new ArticleOneDownload(ArticleDetails.this, article_id));
        t.start();
    }

    public void finishDownloadUI(Article article){
        // Hide progressBar

        // Fulfill Views with data from the article
        if (article != null) {
            String userId = "User Id :" + article.getIdUser();
            //detailUserId.setVisibility(View.VISIBLE);
            if (article.getTitle()!=null) {
                detailTitle.setText(Utils.insertHtmlText(article.getTitle()));
                //detailTitle.setVisibility(View.VISIBLE);
            }
            if (article.getAbstractText()!=null) {
                detailDescription.setText(Utils.insertHtmlText(article.getAbstractText()));
                //detailDescription.setVisibility(View.VISIBLE);
            }
            if(article.getBodyText()!=null){
                detailBody.setText(Utils.insertHtmlText(article.getBodyText()));
                //detailBody.setVisibility(View.VISIBLE);
            }

            // Convert image from string b64 to Bitmap
            if (article.getImage() !=null){
                String image_thumbnail = article.getImage().getImage();
                if(image_thumbnail!=null && !image_thumbnail.isEmpty()) {
                    Bitmap image = Utils.base64StringToImg(image_thumbnail);
                    if(image!=null) {
                        detailImage.setImageBitmap(image);
                    }
                }
            }
            detailImage.setVisibility(View.VISIBLE);

        }

    }
    public void startDownloadUI(){
        Log.i("download","Start downloading");

        // Set view elements on INVISIBLE to avoid strange displays while loading
        detailImage.setVisibility(View.INVISIBLE);

        // Show progressBar

    }


}

