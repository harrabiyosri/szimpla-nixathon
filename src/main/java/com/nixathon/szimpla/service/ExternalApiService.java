package com.nixathon.szimpla.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class ExternalApiService {

    private final RestClient restClient;

    public ExternalApiService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.exemple.com")
                .build();
    }

    public void processJson() {

        JsonNode data = restClient.get()
                .uri("/tasks")
                .retrieve()
                .body(JsonNode.class);

        JsonNode tasks = data.get("tasks");

        if (tasks != null && tasks.isArray()) {
            for (JsonNode task : tasks) {

                String taskId = task.get("id").asText();

                restClient.post()
                        .uri("/complete")
                        .body(Map.of("taskId", taskId))
                        .retrieve()
                        .toBodilessEntity();
            }
        }
    }
}
