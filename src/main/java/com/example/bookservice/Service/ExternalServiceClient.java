package com.example.bookservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExternalServiceClient {

    private final WebClient webClient;

    public ExternalServiceClient(@Value("${bookingservice.url}") String externalServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(externalServiceUrl).build();
    }

    public Mono<String> addBookToExternalService(Long bookId, String token) {
        return this.webClient.post()
                .uri("/api/library/add/{id}", bookId)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response ->
                        Mono.error(new RuntimeException("Не удалось вызвать внешний сервис"))
                )
                .bodyToMono(String.class);
    }
}


