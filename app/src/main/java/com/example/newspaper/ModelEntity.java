package com.example.newspaper;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

import org.json.simple.JSONObject;

import com.example.exceptions.ServerCommunicationError;

public abstract class ModelEntity implements Serializable {
    protected int id;
    protected ModelManager mm;

    public ModelEntity(ModelManager mm){
        this.mm=mm;
    }

    public int getId() {
        return id;
    }

    protected abstract Hashtable<String,String> getAttributes();


    @SuppressWarnings("unchecked")
    public JSONObject toJSON(){
        JSONObject jsonArticle = new JSONObject();
        if(getId()>0)
            jsonArticle.put("id", getId());

        Hashtable<String,String> res = getAttributes();
        Enumeration<String> keys = res.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            jsonArticle.put(key, JSONObject.escape(res.get((key))));
        }
        return jsonArticle;
    }
}
