package com.finshop.finshop.model.dto;

import com.finshop.finshop.model.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String userEmail;
    private List<OrderItemDTO> items;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;
}
