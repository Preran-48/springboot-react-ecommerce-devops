package com.cart.ecom_proj.service;

import com.cart.ecom_proj.model.Product;
import com.cart.ecom_proj.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    // Get all products
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    // Get product by ID
    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    // Add Product
    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {

        // Automatically set availability based on stock
        product.setProductAvailable(product.getStockQuantity() > 0);

        // Image handling
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());

        return repo.save(product);
    }

    // Update Product
    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {

        // Automatically set availability based on stock
        product.setProductAvailable(product.getStockQuantity() > 0);

        // Image handling
        product.setImageData(imageFile.getBytes());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());

        return repo.save(product);
    }

    // Delete Product
    public void deleteProduct(int id) {
        repo.deleteById(id);
    }

    // Search Product
    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
