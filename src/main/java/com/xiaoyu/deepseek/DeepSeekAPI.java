package com.xiaoyu.deepseek;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class DeepSeekAPI {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";

    public static String queryDeepSeek(String message) throws Exception {
        if (DeepSeekConfig.DebugMode) {
            DeepSeek.LOGGER.info("Sending request - Model: {}, Content: {}",
                    DeepSeekConfig.DeepSeeKModel, message);
        }

        // 构建请求JSON
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", DeepSeekConfig.DeepSeeKModel);

        JsonObject messageObj = new JsonObject();
        messageObj.addProperty("role", "user");
        messageObj.addProperty("content", message);

        requestBody.add("messages", new com.google.gson.JsonArray());
        requestBody.getAsJsonArray("messages").add(messageObj);

        // 构建HTTP请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + DeepSeekConfig.DeepSeekAPIKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .timeout(Duration.ofSeconds(30))
                .build();

        // 发送请求并获取响应
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (DeepSeekConfig.DebugMode) {
            DeepSeek.LOGGER.info("Response status: {}", response.statusCode());
            DeepSeek.LOGGER.info("Response content: {}", response.body());
        }

        // 处理响应
        if (response.statusCode() == 200) {
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            String responseContent = jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();

            // 记录请求和响应
            if (DeepSeekConfig.DebugMode) {
                DeepSeek.LOGGER.info("Question: {}", message);
                DeepSeek.LOGGER.info("Answer: {}", responseContent);
            }

            return responseContent;
        } else {
            JsonObject errorResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            String errorMsg = errorResponse.has("error") ?
                    errorResponse.getAsJsonObject("error").get("message").getAsString() :
                    "API错误: " + response.statusCode();
            throw new Exception(errorMsg);
        }
    }
}
