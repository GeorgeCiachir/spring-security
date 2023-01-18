package com.georgeciachir.ch16_17_global_method_security.service;

import com.georgeciachir.ch16_17_global_method_security.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final Map<Integer, Product> products = new HashMap<>();
    private int idCounter;

    @PostConstruct
    public void setUp() {
        products.put(++idCounter, new Product(idCounter, "Toothbrush", 2, "John"));
        products.put(++idCounter, new Product(idCounter, "Wine", 5, "Alex"));
        products.put(++idCounter, new Product(idCounter, "Book", 3, "Jane"));
        System.out.println();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product add(Product product) {
        product.setId(++idCounter);
        products.put(idCounter, product);
        return product;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void remove(int id) {
        products.remove(id);
    }

    @PreFilter("@productService.ownedByLoggedUser(#id, authentication)")
    public void updatePrice(int id, int price) {
        Product found = products.get(id);
        found.setPrice(price);
    }

    @PreAuthorize("hasAuthority('read')")
    public List<Product> getProducts() {
        return new ArrayList<>(products.values());
    }

    @PreAuthorize("hasAuthority('read')")
    public Product get(int id) {
        return products.get(id);
    }

    @PostAuthorize("returnObject.getOwner().equals(authentication.principal.username)")
    public Product getMyProduct(int id) {
        return products.get(id);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PostFilter("filterObject.getOwner().equals(authentication.principal.username)")
    public List<Product> productsForLoggedUser() {
        return new ArrayList<>(products.values());
    }

    @PreFilter("@productService.ownedByLoggedUser(filterObject, authentication)")
    public List<Product> sellProducts(List<Integer> productIds) {
        return productIds.stream()
                .map(products::remove)
                .toList();
    }

    public boolean ownedByLoggedUser(Integer id, Authentication authentication) {
        return products.get(id).getOwner().equals(authentication.getName());
    }
}
