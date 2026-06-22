package com.finshop.finshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private BigDecimal chiffreAffairesTotal;
    private BigDecimal chiffreAffairesMoisCourant;
    private Long nombreCommandesTotal;
    private Long nombreCommandesMoisCourant;
    private List<ProduitVenteDTO> topProduits;
    private List<CaMensuelDTO> caMensuel;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitVenteDTO {
        private String nomProduit;
        private Integer quantiteVendue;
        private BigDecimal chiffreAffaires;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CaMensuelDTO {
        private String mois;
        private BigDecimal chiffreAffaires;
        private Long nombreCommandes;
    }
}
