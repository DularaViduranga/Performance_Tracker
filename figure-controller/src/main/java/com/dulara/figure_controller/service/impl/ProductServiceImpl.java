package com.dulara.figure_controller.service.impl;

import com.dulara.figure_controller.dto.product.ProductResponseDTO;
import com.dulara.figure_controller.repository.oracle.ProductRepository;
import com.dulara.figure_controller.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponseDTO> getProductsByClass(String classCode) {
        List<Map<String, Object>> rawData = productRepository.getProductsByClass(classCode);
        return rawData.stream().map(row -> new ProductResponseDTO(
                (String) row.get("PRD_CODE"),
                (String) row.get("PRD_DESCRIPTION")
        )).toList();
    }
}
