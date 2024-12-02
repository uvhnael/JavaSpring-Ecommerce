package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.uvhnael.ecomapi.Dto.RecommendId;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final WebClient webClient;

    private final String RECOMMEND_API = "http://localhost:1110";

    // Trong hàm getCustomerRecommendation
    public RecommendId getCustomerRecommendation(int customerId, int page, int size) {
        String url = RECOMMEND_API + "/recommendations/" + customerId + "?page=" + page + "&size=" + size;

        // Fetch and map the API response to RecommendId

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(RecommendId.class)
                .onErrorResume(error -> {
                    // Log lỗi (nếu cần)
                    System.err.println("Error fetching recommendations: " + error.getMessage());
                    // Trả về giá trị mặc định
                    return Mono.just(new RecommendId());
                })
                .block();
    }

    // Tương tự với các phương thức còn lại
    public void addNewEvent(int customerId, int productId, int interaction) {
        Map<String, Integer> requestBody = Map.of(
                "customer_id", customerId,
                "product_id", productId,
                "interaction", interaction
        );

        // Gọi API mà không chặn
        webClient.post()
                .uri(RECOMMEND_API + "/add_event")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> {
                    // Log lỗi nếu có
                    System.err.println("Error adding event: " + error.getMessage());
                })
                .subscribe(); // Thực hiện subscribe để bắt đầu xử lý async
    }

    public void addNewCustomer(int customerId) {
        // Gọi API mà không chặn
        webClient.post()
                .uri(RECOMMEND_API + "/add_user/" + customerId)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> {
                    // Log lỗi nếu có
                    System.err.println("Error adding new customer: " + error.getMessage());
                })
                .subscribe(); // Thực hiện subscribe để bắt đầu xử lý async
    }

    public void addNewProduct(int productId, int categoryId) {
        Map<String, Integer> requestBody = Map.of(
                "product_id", productId,
                "category_id", categoryId
        );

        // Gọi API mà không chặn
        webClient.post()
                .uri(RECOMMEND_API + "/add_product")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> {
                    // Log lỗi nếu có
                    System.err.println("Error adding new product: " + error.getMessage());
                })
                .subscribe(); // Thực hiện subscribe để bắt đầu xử lý async
    }

}
