package com.finshop.finshop.service;

import com.finshop.finshop.model.dto.OrderDTO;
import com.finshop.finshop.model.dto.OrderItemDTO;
import com.finshop.finshop.model.entity.Order;
import com.finshop.finshop.model.entity.OrderItem;
import com.finshop.finshop.model.entity.Product;
import com.finshop.finshop.model.entity.User;
import com.finshop.finshop.model.request.OrderRequest;
import com.finshop.finshop.repository.OrderRepository;
import com.finshop.finshop.repository.ProductRepository;
import com.finshop.finshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderDTO createOrder(String userEmail, OrderRequest request) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Créer les lignes de commande
        List<OrderItem> items = request.getItems().stream()
                .map(itemRequest -> {
                    Product product = productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

                    // Vérifier le stock
                    if (product.getStock() < itemRequest.getQuantite()) {
                        throw new RuntimeException("Stock insuffisant pour " + product.getNom());
                    }

                    // Décrémenter le stock
                    product.setStock(product.getStock() - itemRequest.getQuantite());
                    productRepository.save(product);

                    return OrderItem.builder()
                            .product(product)
                            .quantite(itemRequest.getQuantite())
                            .prixUnitaire(product.getPrix())
                            .build();
                })
                .collect(Collectors.toList());

        // Calculer le total
        BigDecimal total = items.stream()
                .map(item -> item.getPrixUnitaire()
                        .multiply(BigDecimal.valueOf(item.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Créer la commande
        Order order = Order.builder()
                .user(user)
                .items(items)
                .total(total)
                .build();

        // Lier les items à la commande
        items.forEach(item -> item.setOrder(order));

        return toDTO(orderRepository.save(order));
    }

    public List<OrderDTO> getUserOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        return toDTO(orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée")));
    }

    private OrderDTO toDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .id(item.getId())
                        .productNom(item.getProduct().getNom())
                        .quantite(item.getQuantite())
                        .prixUnitaire(item.getPrixUnitaire())
                        .sousTotal(item.getPrixUnitaire()
                                .multiply(BigDecimal.valueOf(item.getQuantite())))
                        .build())
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .userEmail(order.getUser().getEmail())
                .items(itemDTOs)
                .status(order.getStatus())
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
