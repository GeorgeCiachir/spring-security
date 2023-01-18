package com.georgeciachir.ch16_17_global_method_security.controller;

import com.georgeciachir.ch16_17_global_method_security.model.Product;
import com.georgeciachir.ch16_17_global_method_security.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public Product add(@RequestBody Product product) {
        return productService.add(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        productService.remove(id);
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable int id, @RequestParam int price) {
        productService.updatePrice(id, price);
    }

    @GetMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }


    @GetMapping("/{id}")
    public Product getProduct(@PathVariable int id) {
        return productService.get(id);
    }


    @GetMapping("/my-products/{id}")
    public Product getMyProduct(@PathVariable int id) {
        return productService.getMyProduct(id);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @GetMapping("/my-products")
    public List<Product> productsForLoggedUser() {
        return productService.productsForLoggedUser();
    }

    @PostMapping("/sell")
    public List<Product> sell(@RequestParam List<Integer> productIds) {
        return productService.sellProducts(productIds);
    }
}
