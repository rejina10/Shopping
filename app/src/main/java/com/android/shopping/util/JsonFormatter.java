package com.android.shopping.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rejina on 11/14/2015.
 */
public class JsonFormatter {

    public String convertToJson(String categoryName, String itemName){
        try {
            JSONObject obj = new JSONObject();
            obj.put("name",itemName);
            obj.put("category",categoryName);

            JSONObject objItem = new JSONObject();
            objItem.put("item",obj);

            return objItem.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}

