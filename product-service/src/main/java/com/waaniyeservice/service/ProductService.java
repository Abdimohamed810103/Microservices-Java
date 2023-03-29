package com.waaniyeservice.service;

import com.waaniyeservice.dto.ProductRequest;
import com.waaniyeservice.dto.ProductResponse;
import com.waaniyeservice.model.Product;
import com.waaniyeservice.repository.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Data
@Service
public class ProductService {

    private final ProductRepo repo;
    public void createProduct(ProductRequest productRequest){
        Product product = Product
                .builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        repo.save(product);

        log.info("Product {} is saved to the database", product.getId());
    }


    public List<ProductResponse> getAllproduct() {
        List<Product> products = repo.findAll();
        List<ProductResponse> responseList = products.stream().map(p -> productsToResponse(p)).collect(Collectors.toList());
        return responseList;
    }

    private ProductResponse productsToResponse(Product product) {
      ProductResponse productResponse = ProductResponse.builder()
              .id(product.getId())
              .name(product.getName())
              .description(product.getDescription())
              .price(product.getPrice())
              .build();
      return productResponse;
    }
}
