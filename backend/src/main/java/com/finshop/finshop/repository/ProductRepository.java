package com.finshop.finshop.repository;

import com.finshop.finshop.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNomContainingIgnoreCase(String nom);
    List<Product> findByStockGreaterThan(Integer stock);

}
