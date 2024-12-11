package com.example.newspaper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<Article> allArticles = new ArrayList<>();
    private List<Article> filteredArticles = new ArrayList<>();

    private String[] categories = {"All", "National", "Economy", "Sports", "Technology"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.main_activity);

        // Download articles
        Thread t = new Thread(new ArticlesDownload(MainActivity.this));
        t.start();

        // Setup buttons to filter articles
        Button buttonAll = findViewById(R.id.buttonAll);
        Button buttonNational = findViewById(R.id.buttonNational);
        Button buttonEconomy = findViewById(R.id.buttonEconomy);
        Button buttonSports = findViewById(R.id.buttonSports);
        Button buttonTechnology = findViewById(R.id.buttonTechnology);

        buttonAll.setOnClickListener(v -> filterArticlesByCategory("All"));
        buttonNational.setOnClickListener(v -> filterArticlesByCategory("National"));
        buttonEconomy.setOnClickListener(v -> filterArticlesByCategory("Economy"));
        buttonSports.setOnClickListener(v -> filterArticlesByCategory("Sports"));
        buttonTechnology.setOnClickListener(v -> filterArticlesByCategory("Technology"));
    }

    public void finishDownloadUI(List<Article> data) {
        ProgressBar pb = findViewById(R.id.main_pb);
        pb.setVisibility(View.INVISIBLE);

        recyclerView = findViewById(R.id.recyclerView);

        this.allArticles.clear();
        this.allArticles.addAll(data);
        this.filteredArticles.addAll(data);

        adapter = new NewsAdapter(this, filteredArticles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void startDownloadUI(){
        ProgressBar pb = findViewById(R.id.main_pb);
        pb.setVisibility(View.VISIBLE);
    }

    private void filterArticlesByCategory(String category) {
        filteredArticles.clear();

        if (category.equals("All")) {
            filteredArticles.addAll(allArticles);
        } else {
            for (Article article : allArticles) {
                if (article.getCategory().equalsIgnoreCase(category)) {
                    filteredArticles.add(article);
                }
            }
        }

        if (filteredArticles.isEmpty()) {
            Toast.makeText(this, "No articles found for this category", Toast.LENGTH_SHORT).show();
        }

        adapter.updateData(filteredArticles);
    }
}
