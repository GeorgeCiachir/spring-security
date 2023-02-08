package com.georgeciachir.ch18_oauth2_app_resource_server.controller;

import com.georgeciachir.ch18_oauth2_app_resource_server.entity.Product;
import com.georgeciachir.ch18_oauth2_app_resource_server.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product add(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping
    public List<Product> findProductsForLoggedUser() {
        return productService.findProductsForLoggedUser();
    }

//    TODO: Map the roles from the Auth0 server and the permissions from both Keycloak and Auth0
//    @PreAuthorize(
//            "hasRole('imperial-admin')" +
//            "(hasPermission('delete-product') and @productController.isOwnProduct(#id, authentication))")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    public boolean isOwnProduct(Integer id, Authentication authentication) {
        String username = authentication.name();
        return productService.findById(id)
                .map(Product::getName)
                .filter(username::equals)
                .isPresent();
    }
}
