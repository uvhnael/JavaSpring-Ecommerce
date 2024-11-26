package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.uvhnael.ecomapi.Dto.RecommendId;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final WebClient webClient;

    private final String RECOMMEND_API = "http://localhost:1110";

    public RecommendId getCustomerRecommendation(int customerId, int page, int size) {
        String url = RECOMMEND_API + "/recommendations/" + customerId + "?page=" + page + "&size=" + size;

        // Fetch and map the API response to RecommendId
        RecommendId recommendId = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(RecommendId.class) // Map directly to the class
                .block(); // Blocking for simplicity; avoid in reactive environments

        return recommendId;
    }

    public void addNewEvent(int customerId, int productId, int interaction) {
        Map<String, Integer> requestBody = Map.of(
                "customer_id", customerId,
                "product_id", productId,
                "interaction", interaction
        );
        webClient.post()
                .uri(RECOMMEND_API + "/add_event")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void addNewCustomer(int customerId) {
        webClient.post()
                .uri(RECOMMEND_API + "/add_user/" + customerId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void addNewProduct(int productId, int categoryId) {
        Map<String, Integer> requestBody = Map.of(
                "product_id", productId,
                "category_id", categoryId
        );
        webClient.post()
                .uri(RECOMMEND_API + "/add_product")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
