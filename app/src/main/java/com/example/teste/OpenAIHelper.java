package com.example.teste;

import android.os.AsyncTask;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAIHelper {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    public interface Callback {
        void onSuccess(String response);
        void onError(String error);
    }
    public static void gerarPlanoAlimentar(String peso, String altura, String objetivo, Callback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    OkHttpClient client = new OkHttpClient();

                    // Create the request payload
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("model", "gpt-3.5-turbo");
                    jsonBody.put("messages", new JSONArray()
                            .put(new JSONObject()
                                    .put("role", "user")
                                    .put("content", "Cria um plano alimentar para uma pessoa com " +
                                            peso + " kg, " + altura + " m de altura, e cujo objetivo é " + objetivo + ".")
                            )
                    );
                    jsonBody.put("max_tokens", 500);
                    jsonBody.put("temperature", 0.7);
                    jsonBody.put("top_p", 1);

                    // Log the request payload for debugging
                    String jsonPayload = jsonBody.toString();
                    Log.d("OpenAIHelper", "Request Payload: " + jsonPayload);

                    // Create the request
                    RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json; charset=utf-8"));
                    Request request = new Request.Builder()
                            .url(API_URL)
                            .addHeader("Authorization", "Bearer " + API_KEY)
                            .post(body)
                            .build();

                    // Execute the request
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "{\"error\":\"Erro na requisição: " + response.code() + " - " + response.message() + "\"}";
                    }
                } catch (IOException | org.json.JSONException e) {
                    Log.e("OpenAIHelper", "Erro ao enviar requisição", e);
                    return "{\"error\":\"" + e.getMessage() + "\"}";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    // Verificar se há erro
                    if (result.contains("\"error\"")) {
                        callback.onError(new JSONObject(result).getString("error"));
                        return;
                    }

                    // Processar a resposta JSON
                    JSONObject jsonResponse = new JSONObject(result);
                    String text = jsonResponse.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");
                    callback.onSuccess(text.trim());
                } catch (Exception e) {
                    callback.onError("Erro ao processar a resposta: " + e.getMessage());
                }
            }
        }.execute();
    }
}
