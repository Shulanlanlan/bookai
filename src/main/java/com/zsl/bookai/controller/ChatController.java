package com.zsl.bookai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsl.bookai.model.PromptRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController extends BaseController{

    private final OkHttpClient client = new OkHttpClient();

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody PromptRequest req) {
        try {
            String body = new ObjectMapper().writeValueAsString(Map.of(
                "model", "deepseek-chat",
                "messages", List.of(
                    Map.of("role", "system", "content", req.systemPrompt),
                    Map.of("role", "user", "content", req.userPrompt)
                ),
                "temperature", 0
            ));

            String apiKey = "TODO 待填写";
            Request httpRequest = new Request.Builder()
                .url("https://api.deepseek.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(okhttp3.RequestBody.create(body, MediaType.get("application/json")))
                .build();

            Response response = client.newCall(httpRequest).execute();
            if (response.isSuccessful() && response.body() != null) {
                return ResponseEntity.ok(response.body().string());
            } else {
                return ResponseEntity.status(502).body("AI接口调用失败");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("服务器内部错误：" + e.getMessage());
        }
    }
}
