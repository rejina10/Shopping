package com.android.shopping;

import android.content.ClipData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by rejina on 11/12/2015.
 */
public class HttpConnection {

    private static final String url = "https://czshopper.herokuapp.com/items.json";
    private static final String urlUpdate = "https://czshopper.herokuapp.com/items/";
    private static final String authKey = "AvbuVi9aXbphKFxhLyaB";

    public static List<ItemModel> executeHttpRequest() throws Exception, Error {
        String output = null;

        ItemModel model;
        List<ItemModel> result = new ArrayList<ItemModel>();

        try {
            URL u = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-CZ-Authorization",authKey);
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            int responsecode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responsecode == 200) {
                Gson gson =new Gson();
                TypeToken<List<ItemModel>> token = new TypeToken<List<ItemModel>>() {};
                result = gson.fromJson(response.toString(), token.getType());
                System.out.println("Response: " + response.toString());
            }

            return result;
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public static boolean executePostRequest(String message){
        BufferedReader br = null;
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setReadTimeout(10000 /*milliseconds*/);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-CZ-Authorization", authKey);
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(message);
            out.close();
            int responseCode = conn.getResponseCode();
            if(responseCode== HttpURLConnection.HTTP_CREATED){
                return  true;
//                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//                while ((inputLine = br.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                br.close();

            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static boolean executePutRequest(String message, String id) {

        try {
            URL u = new URL(urlUpdate + id + ".json");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setReadTimeout(10000 /*milliseconds*/);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-CZ-Authorization",authKey);
            conn.setRequestProperty("Accept", "application/json");

            conn.connect();
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(message);
            out.close();
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                return true;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public static Boolean executeDeleteRequest(String id) {

        try {
            URL u = new URL(urlUpdate + id + ".json");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setReadTimeout(10000 /*milliseconds*/);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-CZ-Authorization",authKey);
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            System.out.println(conn.getResponseCode());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
