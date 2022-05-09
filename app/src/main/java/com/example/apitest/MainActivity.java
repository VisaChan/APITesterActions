package com.example.apitest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    private String URL = "https://opentdb.com/api.php";

    private RequestQueue queue;

    private ArrayList<Question> qList = new ArrayList<Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this.getApplicationContext());

        tv = findViewById(R.id.APIContent);

        callAPI();

    }

    public void sendMessage(View view){
        final String[] abResults = new String[1];
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(qList.get(1).getQuestion());
        String[] answers = new String[qList.get(1).getAnswers().size()];
        for(int i = 0; i < qList.get(1).getAnswers().size(); i++){
            answers[i] = qList.get(1).getAnswers().get(i);
        }
        ab.setItems(answers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                String item = answers[which];
                String results = item + " is Correct!";
                if(item.equals(qList.get(1).getCorrect()) == false){
                    results = item + " is Wrong. The answer is " + qList.get(1).getCorrect();
                }
                abResults[0] = results;
                System.out.println(results);
                callsecond(abResults[0]);
            }
        });
        ab.setCancelable(false);
        ab.show();


    }

    private void callsecond (String results){
        AlertDialog.Builder ab2 = new AlertDialog.Builder(this);
        ab2.setTitle(results);
        ab2.setPositiveButton("OK", null);
        ab2.setCancelable(false);
        ab2.show();
    }

    private void callAPI(){
        Uri.Builder buildURL = Uri.parse(URL).buildUpon();
        buildURL.appendQueryParameter("amount", "10");
        buildURL.appendQueryParameter("category", "23");
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray sources = response.getJSONArray("results");
                    int size = sources.length();
                    for(int i=0; i<sources.length(); i++){
                        String q = ((JSONObject)sources.get(i)).getString("question");
                        String c = (((JSONObject)sources.get(i)).getString("correct_answer"));
                        JSONArray a = (((JSONObject)sources.get(i)).getJSONArray("incorrect_answers"));
                        ArrayList<String> al = new ArrayList<String>();
                        al.add(c);
                        for(int j = 0; j < a.length(); j++){
                            String answer = a.get(j).toString();
                            al.add(answer);
                        }

                        Question news = new Question(q,c,al);
                        qList.add(news);

                        StringBuilder sb = new StringBuilder();
                        for(Question questionItem: qList){
                            sb.append(questionItem.getQuestion() + "\n \n");
                        }
                        String s = sb.toString();
                        System.out.println(s);
                        //tv.setText(s);
                    }
                } catch (Exception e) {
                    System.out.println("Error in response Volley");
                }
            }

        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*try {
                    //JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    System.out.println("Error Listener Invoked");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
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
