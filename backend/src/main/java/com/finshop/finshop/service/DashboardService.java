package com.finshop.finshop.service;

import com.finshop.finshop.model.dto.DashboardDTO;
import com.finshop.finshop.model.entity.Order;
import com.finshop.finshop.model.entity.OrderItem;
import com.finshop.finshop.repository.OrderRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderRepository orderRepository;

    public DashboardDTO getDashboard() {
        List<Order> allOrders = orderRepository.findAll();
        LocalDateTime debutMois = LocalDateTime.now().withDayOfMonth(1).withHour(0);

        List<Order> commandesMois = allOrders.stream()
                .filter(o -> o.getCreatedAt().isAfter(debutMois))
                .collect(Collectors.toList());

        BigDecimal caTotal = allOrders.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal caMois = commandesMois.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardDTO.builder()
                .chiffreAffairesTotal(caTotal)
                .chiffreAffairesMoisCourant(caMois)
                .nombreCommandesTotal((long) allOrders.size())
                .nombreCommandesMoisCourant((long) commandesMois.size())
                .topProduits(getTopProduits(allOrders))
                .caMensuel(getCaMensuel(allOrders))
                .build();
    }

    private List<DashboardDTO.ProduitVenteDTO> getTopProduits(List<Order> orders) {
        Map<String, int[]> produitStats = new HashMap<>();
        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                String nomProduit = item.getProduct().getNom();
                produitStats.computeIfAbsent(nomProduit, k -> new int[]{0});
                produitStats.get(nomProduit)[0] += item.getQuantite();
            }
        }
        return produitStats.entrySet().stream()
                .sorted((a, b) -> b.getValue()[0] - a.getValue()[0])
                .limit(5)
                .map(entry -> DashboardDTO.ProduitVenteDTO.builder()
                        .nomProduit(entry.getKey())
                        .quantiteVendue(entry.getValue()[0])
                        .chiffreAffaires(BigDecimal.valueOf(entry.getValue()[0]))
                        .build())
                .collect(Collectors.toList());
    }

    private List<DashboardDTO.CaMensuelDTO> getCaMensuel(List<Order> orders) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        return orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getCreatedAt().format(formatter),
                        Collectors.toList()
                ))
                .entrySet().stream()
                .map(entry -> DashboardDTO.CaMensuelDTO.builder()
                        .mois(entry.getKey())
                        .chiffreAffaires(entry.getValue().stream()
                                .map(Order::getTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                        .nombreCommandes((long) entry.getValue().size())
                        .build())
                .sorted(Comparator.comparing(DashboardDTO.CaMensuelDTO::getMois))
                .collect(Collectors.toList());
    }
}
