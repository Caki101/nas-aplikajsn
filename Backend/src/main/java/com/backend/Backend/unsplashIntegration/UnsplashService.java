package com.backend.Backend.unsplashIntegration;

import com.backend.Backend.security.SecurityData;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UnsplashService {

    final WebClient webClient;
    static final String UNSPLASH_API_URL = "https://api.unsplash.com/";
    static final String UNSPLASH_CLIENT_ID = SecurityData.UCI;

    public UnsplashService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(UNSPLASH_API_URL).build();
    }

    public Mono<String> getPhotoUrl(String query) {
        return webClient.get()
                .uri("/search/photos?{query}&cliend_id={UNSPLASH_CLIENT_ID}", query, UNSPLASH_CLIENT_ID)
                .retrieve()
                .bodyToMono(UnsplashResponse.class)
                .map(response -> response.getResults().getFirst().getUrls().getRegular());
    }
}
