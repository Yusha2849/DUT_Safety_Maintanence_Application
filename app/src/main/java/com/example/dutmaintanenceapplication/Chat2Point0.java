package com.example.dutmaintanenceapplication;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;

public class Chat2Point0 {

    private TextView responseTextView;

    public Chat2Point0(TextView responseTextView) {
        this.responseTextView = responseTextView;
    }

    public void getGPTResponse(String message) {
        new GPTRequestTask().execute(message);
    }

    private class GPTRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String apiKey = "sk-proj-aLP6gQK04kZngAFhkKgfT3BlbkFJzIgnosda0bxJHDNFr5UH";
            String modelId = "gpt-3.5-turbo";
            String url = "https://api.openai.com/v1/chat/completions";

            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Authorization", "Bearer " + apiKey);
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());

                String jsonData = "{\"messages\":[{\"role\":\"system\",\"content\":\"You are going to receive an issue description from a Safety and maintenance Reporting App. Provide Safety Tips consisting of 1-2 Points in point form. DO NOT Provide any Tip that suggests Reporting the issue.\"}," +
                        "{\"role\":\"user\",\"content\":\"" + params[0] + "\"}]," +
                        "\"model\":\"" + modelId + "\"}";

                wr.writeBytes(jsonData);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jobj = new JSONObject(response.toString());
                JSONArray choices = jobj.getJSONArray("choices");
                String new_response = (choices.getJSONObject(0).getJSONObject("message").getString("content"));
                return new_response;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                responseTextView.setText(result);
            } else {
                responseTextView.setText("Error occurred. Please try again later.");
            }
        }
    }
}