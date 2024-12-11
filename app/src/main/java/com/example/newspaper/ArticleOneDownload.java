package com.example.newspaper;

import android.util.Log;
import com.example.exceptions.AuthenticationError;
import java.util.Properties;

public class ArticleOneDownload implements Runnable{
    private ArticleDetails aa;

    private int articleId;
    private final String service_url="https://sanger.dia.fi.upm.es/pui-rest-news/";
    private final String user="DEV_TEAM_10";
    private final String password = "123456@10";

    public ArticleOneDownload(ArticleDetails aa, int articleId){
        this.aa = aa;
        this.articleId = articleId;
    }

    @Override
    public void run() {
        aa.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                aa.startDownloadUI();
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

        Article article = mm.getArticle(articleId);
        Log.i("detail_article", article.toString());
        aa.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                aa.finishDownloadUI(article);
            }
        });
    }
}
