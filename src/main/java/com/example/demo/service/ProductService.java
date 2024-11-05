package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired  // Inject the repository
    private ProductRepo repo;

    public List<Product> getAllProduct() {
        return repo.findAll(); // Fetch all products from the repository
    }

    public Product getProdId(int id) {
        return repo.findById(id).orElse(null); // Return null if product not found
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        // Set image properties
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());

        return repo.save(product); // Save product to MySQL database
    }

    public Product updateProd(int id, Product product, MultipartFile imageFile) throws IOException {
        // Retrieve existing product
        Optional<Product> existingProductOpt = repo.findById(id);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();

            // Update existing fields only if necessary
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setReleaseDate(product.getReleaseDate());
            existingProduct.setProductAvailable(product.isProductAvailable());
            existingProduct.setStockQuantity(product.getStockQuantity());

            // Update image if a new file is provided
            if (imageFile != null && !imageFile.isEmpty()) {
                existingProduct.setImageName(imageFile.getOriginalFilename());
                existingProduct.setImageType(imageFile.getContentType());
                existingProduct.setImageData(imageFile.getBytes());
            }

            return repo.save(existingProduct); // Save updated product to MySQL database
        }
        return null; // Return null if product not found
    }

    public void deleteProd(int id) {
        repo.deleteById(id);
    }

    public List<Product> searchProduct(String keyword) {
        return repo.searchProd(keyword);
    }
}
