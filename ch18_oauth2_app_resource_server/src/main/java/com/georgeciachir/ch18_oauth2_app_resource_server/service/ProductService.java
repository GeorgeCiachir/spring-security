package com.georgeciachir.ch18_oauth2_app_resource_server.service;

import com.georgeciachir.ch18_oauth2_app_resource_server.entity.Product;
import com.georgeciachir.ch18_oauth2_app_resource_server.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @PreAuthorize("#product.getOwner() == authentication.getName()")
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findProductsForLoggedUser() {
        return productRepository.findAllByOwner();
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

}
