package com.example.newspaper;

import android.util.Log;

import com.example.exceptions.AuthenticationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ArticlesDownload implements Runnable{
        MainActivity ma;
        private final String service_url="https://sanger.dia.fi.upm.es/pui-rest-news/";
        private final String user="DEV_TEAM_10";
        private final String password = "123456@10";

        public ArticlesDownload(MainActivity ma){
            this.ma = ma;
        }

        @Override
        public void run() {
            ma.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ma.startDownloadUI();
                }
            });

            Properties prop = new Properties();
            prop.setProperty(ModelManager.ATTR_LOGIN_USER, user);
            prop.setProperty(ModelManager.ATTR_LOGIN_PASS, password);
            prop.setProperty(ModelManager.ATTR_SERVICE_URL, service_url);
            prop.setProperty(ModelManager.ATTR_REQUIRE_SELF_CERT, "TRUE");

            ModelManager mm = null;

            try{
                mm = new ModelManager(prop);
            }catch (AuthenticationError e) {
                Log.e("authentication error", e.getMessage());
                System.exit(-1);
            }

            // get list of articles for logged user
            List<Article> article_list = mm.getArticles();
            // Filter non-relevant articles
            List<Article> filteredArticles = new ArrayList<>();
            for (Article article : article_list) {
                if (isRelevantArticle(article)) {
                    filteredArticles.add(article);
                }
            }
            for (Article article : article_list) {
                System.out.println(article);
                Log.i("articles", article.toString());
            }
            ma.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ma.finishDownloadUI(filteredArticles);
                }
            });
        }

        public Boolean isRelevantArticle(Article article){
            return !article.getTitle().contains("fasd");
        }
}
