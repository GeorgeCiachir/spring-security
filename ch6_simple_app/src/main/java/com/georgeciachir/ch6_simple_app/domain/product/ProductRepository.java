package com.georgeciachir.ch6_simple_app.domain.product;

import com.georgeciachir.ch6_simple_app.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
