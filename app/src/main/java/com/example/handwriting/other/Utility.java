package com.example.handwriting.other;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utility {
    public static String handleResponse(String response){
        try {
            JSONArray jsonArray=new JSONArray(response);
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String s=jsonObject.getString("STATE");
            return s;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
