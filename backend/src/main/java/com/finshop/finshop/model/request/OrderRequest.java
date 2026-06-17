package com.finshop.finshop.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @NotNull
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {

        @NotNull(message = "Le produit est obligatoire")
        private Long productId;

        @NotNull
        @Min(value = 1, message = "La quantité doit être au moins 1")
        private Integer quantite;
    }
}
