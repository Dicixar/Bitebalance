package com.example.teste;

import android.os.AsyncTask;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import android.util.Log;
import org.json.JSONArray;

// Classe OpenAIHelper que facilita a interação com a API da OpenAI para gerar um plano alimentar
public class OpenAIHelper {

    // URL da API da OpenAI
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    // Chave de API da OpenAI (substitua pela sua chave válida)
    private static final String API_KEY = "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";

    // Interface de callback para retornar o resultado da requisição
    public interface Callback {
        void onSuccess(String response); // Chamado quando a requisição é bem-sucedida
        void onError(String error); // Chamado quando ocorre um erro
    }

    // Método para gerar um plano alimentar com base no peso, altura e objetivo do utilizador
    public static void gerarPlanoAlimentar(String peso, String altura, String objetivo, Callback callback) {
        // Executa a requisição em segundo plano usando AsyncTask
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    OkHttpClient client = new OkHttpClient(); // Cria um cliente HTTP

                    // Cria o corpo da requisição em formato JSON
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("model", "gpt-3.5-turbo"); // Define o modelo da OpenAI a ser usado
                    jsonBody.put("messages", new JSONArray()
                            .put(new JSONObject()
                                    .put("role", "user") // Define o papel do utilizador
                                    .put("content", "Cria um plano alimentar para uma pessoa com " +
                                            peso + " kg, " + altura + " m de altura, e cujo objetivo é " + objetivo + ".") // Define a mensagem do utilizador
                            )
                    );
                    jsonBody.put("max_tokens", 500); // Define o número máximo de tokens na resposta
                    jsonBody.put("temperature", 0.7); // Define a temperatura (controla a criatividade da resposta)
                    jsonBody.put("top_p", 1); // Define o parâmetro top_p (controla a diversidade da resposta)

                    // Log do payload da requisição para depuração
                    String jsonPayload = jsonBody.toString();
                    Log.d("OpenAIHelper", "Request Payload: " + jsonPayload);

                    // Cria o corpo da requisição HTTP
                    RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json; charset=utf-8"));

                    // Cria a requisição HTTP
                    Request request = new Request.Builder()
                            .url(API_URL) // Define a URL da API
                            .addHeader("Authorization", "Bearer " + API_KEY) // Adiciona o cabeçalho de autorização
                            .post(body) // Define o método POST
                            .build();

                    // Executa a requisição e obtém a resposta
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string(); // Retorna a resposta em caso de sucesso
                    } else {
                        return "{\"error\":\"Erro na requisição: " + response.code() + " - " + response.message() + "\"}"; // Retorna uma mensagem de erro
                    }
                } catch (IOException | org.json.JSONException e) {
                    Log.e("OpenAIHelper", "Erro ao enviar requisição", e); // Log de erro
                    return "{\"error\":\"" + e.getMessage() + "\"}"; // Retorna uma mensagem de erro
                }
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    // Verifica se a resposta contém um erro
                    if (result.contains("\"error\"")) {
                        callback.onError(new JSONObject(result).getString("error")); // Chama o callback de erro
                        return;
                    }

                    // Processa a resposta JSON
                    JSONObject jsonResponse = new JSONObject(result);
                    String text = jsonResponse.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content"); // Extrai o conteúdo da resposta
                    callback.onSuccess(text.trim()); // Chama o callback de sucesso com o texto do plano alimentar
                } catch (Exception e) {
                    callback.onError("Erro ao processar a resposta: " + e.getMessage()); // Chama o callback de erro
                }
            }
        }.execute(); // Executa a AsyncTask
    }
}