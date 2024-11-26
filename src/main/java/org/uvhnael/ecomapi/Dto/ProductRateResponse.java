package org.uvhnael.ecomapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRateResponse {
    private List<Long> productId;
    private List<Integer> count;

    public void sortByCount() {
        for (int i = 0; i < count.size(); i++) {
            for (int j = i + 1; j < count.size(); j++) {
                if (count.get(i) < count.get(j)) {
                    int temp = count.get(i);
                    count.set(i, count.get(j));
                    count.set(j, temp);

                    Long tempId = productId.get(i);
                    productId.set(i, productId.get(j));
                    productId.set(j, tempId);
                }
            }
        }
    }
}
