package com.example.newspaper;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.media.ImageReader;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicReference;

import org.json.simple.JSONObject;
import com.example.exceptions.ServerCommunicationError;

public class Article extends ModelEntity implements Serializable {

    private String title;

    private String subtitle;
    private String category;
    private String abstractText;
    private String bodyText;
    private int idUser;
    private Image mainImage;
    private String imageDescription;
    private String thumbnail;

    private Date publicationDate;

    private String parseStringFromJson(JSONObject jsonArticle, String key, String def){
        Object in = jsonArticle.getOrDefault(key,def);
        return (in==null?def:in).toString();
    }

    @SuppressWarnings("unchecked")
    protected Article(ModelManager mm, JSONObject jsonArticle){
        super(mm);
        try{
            id = Integer.parseInt(jsonArticle.get("id").toString());
            idUser = Integer.parseInt(parseStringFromJson(jsonArticle,"id_user","0"));
            title = parseStringFromJson(jsonArticle,"title","").replaceAll("\\\\","");
            subtitle = parseStringFromJson(jsonArticle,"subtitle","").replaceAll("\\\\","");

            category = parseStringFromJson(jsonArticle,"category","").replaceAll("\\\\","");
            abstractText = parseStringFromJson(jsonArticle,"abstract","").replaceAll("\\\\","");
            bodyText = parseStringFromJson(jsonArticle,"body","").replaceAll("\\\\","");

            imageDescription = parseStringFromJson(jsonArticle,"image_description","").replaceAll("\\\\","");
            thumbnail = parseStringFromJson(jsonArticle,"thumbnail_image","").replaceAll("\\\\","");
            publicationDate = Utils.dateFromString(parseStringFromJson(jsonArticle, "update_date","").replaceAll("\\\\",""));

            String imageData = parseStringFromJson(jsonArticle,"image_data","").replaceAll("\\\\","");

            if (imageData!=null && !imageData.isEmpty())
                mainImage = new Image(mm, 1, imageDescription, id, imageData);
        }catch(Exception e){

            throw new IllegalArgumentException("ERROR: Error parsing Article: from json"+jsonArticle);
        }
    }

    public Article(ModelManager mm, String category, String title, String subtitle, String abstractText, String body, String footer, String publicationDate){
        super(mm);
        id = -1;
        this.category = category;
        idUser = Integer.parseInt(mm.getIdUser());
        this.abstractText = abstractText;
        this.title = title;
        this.subtitle = subtitle;
        bodyText = body;

    }

    public void setId(int id){
        if (id <1){
            throw new IllegalArgumentException("ERROR: Error setting a wrong id to an article:"+id);
        }
        if (this.id>0 ){
            throw new IllegalArgumentException("ERROR: Error setting an id to an article with an already valid id:"+this.id);
        }
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle(){ return subtitle; }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category= category;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setSubTitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAbstractText() {
        return abstractText;
    }
    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }
    public String getBodyText() {
        return bodyText;
    }
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public int getIdUser(){
        return idUser;
    }
    public Image getImage() throws ServerCommunicationError {
        Image image = mainImage;
        if (mainImage==null && thumbnail!=null && !thumbnail.isEmpty()){
            image = new Image(mm,1,"",getId(),thumbnail);
        }
        return image;
    }
    public void setImage(Image image) {
        this.mainImage = image;
    }

    public Image addImage(String b64Image, String description) throws ServerCommunicationError{
        int order = 1;
        Image img =new Image(mm, order, description, getId(), b64Image);
        mainImage= img;

        return img;
    }


    @Override
    public String toString() {
        return "Article [id=" + getId()
                + ", userId=" + idUser
                //+ "isPublic=" + isPublic + ", isDeleted=" + isDeleted
                +", category=" + category
                +", titleText=" + title
                +", subTitleText=" + (subtitle!=null ? subtitle : "")
                +", abstractText=" + abstractText
                +  ", bodyText="	+ bodyText
                +", image_description=" + imageDescription
                +", image_data=" + mainImage
                +", thumbnail=" + thumbnail
                + "]";
    }

    @SuppressLint("SuspiciousIndentation")
    public Hashtable<String,String> getAttributes(){
        Hashtable<String,String> res = new Hashtable<String,String>();
        //res.put("is_public", ""+(isPublic?1:0));
        //res.put("id_user", ""+idUser);
        res.put("category", category);
        res.put("abstract", abstractText);
        res.put("title", title);
        //res.put("is_deleted", ""+(isDeleted?1:0));
        res.put("body", bodyText);
        if (mainImage!=null){
            res.put("image_data", mainImage.getImage());
            res.put("image_media_type", "image/png");
        }

        if (mainImage!=null && mainImage.getDescription()!=null && !mainImage.getDescription().isEmpty())
            res.put("image_description", mainImage.getDescription());
        else if (imageDescription!=null && !imageDescription.isEmpty())
            res.put("image_description", imageDescription);

        return res;
    }
}
