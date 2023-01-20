package com.georgeciachir.ch18_oauth2_app_resource_server.repository;

import com.georgeciachir.ch18_oauth2_app_resource_server.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Of course, an alternative would be to get the logged username in the service from the
    // Authentication (maybe using SecurityContextHolder) and pass it here as a method param
    @Query("SELECT p FROM Product p WHERE p.owner = ?#{authentication.name}")
    List<Product> findAllByOwner();


    @PreAuthorize(
            "#product.getOwner().equals(authentication.getName()) and " +
                    "#oauth2.hasScope('product-management-backend')")
    @Override
    Product save(Product product);
}
