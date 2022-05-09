package com.example.apitest;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionMaker {

    public static void callAPI(MainActivity ma, QuestionList ql){

        String URL = "https://opentdb.com/api.php";

        RequestQueue queue = Volley.newRequestQueue(ma.getApplicationContext());

        Uri.Builder buildURL = Uri.parse(URL).buildUpon();
        buildURL.appendQueryParameter("amount", "10");
        buildURL.appendQueryParameter("category", "23");
        String urlToUse = buildURL.build().toString();
        System.out.println(urlToUse);

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray sources = response.getJSONArray("results");

                    for (int i = 0; i < sources.length(); i++) {
                        String q = ((JSONObject) sources.get(i)).getString("question");
                        String c = (((JSONObject) sources.get(i)).getString("correct_answer"));
                        JSONArray a = (((JSONObject) sources.get(i)).getJSONArray("incorrect_answers"));
                        ArrayList<String> al = new ArrayList<String>();
                        al.add(c);
                        for (int j = 0; j < a.length(); j++) {
                            String answer = a.get(j).toString();
                            al.add(answer);
                        }
                        Question news = new Question(q, c, al);
                        ql.add(news);
                    }

                } catch (Exception e) {
                    System.out.println("Error in response Volley");
                }
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error Listener Invoked");
            }
        };

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

}
