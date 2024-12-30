package com.example.teste;

import android.os.AsyncTask;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import android.util.Log;

public class OpenAIHelper {

    private static final String API_URL = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "";

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

                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("model", "text-davinci-003");
                    jsonBody.put("prompt", "Cria um plano alimentar para uma pessoa com " +
                            peso + " kg, " + altura + " cm de altura, e cujo objetivo é " + objetivo + ".");
                    jsonBody.put("max_tokens", 500);
                    jsonBody.put("temperature", 0.7); // Criatividade
                    jsonBody.put("top_p", 1);        // Probabilidade acumulada

                    RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
                    Request request = new Request.Builder()
                            .url(API_URL)
                            .addHeader("Authorization", "Bearer " + API_KEY)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "{\"error\":\"Erro na requisição: " + response.code() + "\"}";
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
                            .getString("text");
                    callback.onSuccess(text.trim());
                } catch (Exception e) {
                    callback.onError("Erro ao processar a resposta: " + e.getMessage());
                }
            }
        }.execute();
    }
}
