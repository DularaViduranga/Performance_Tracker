package com.dulara.figure_controller.service;

import com.dulara.figure_controller.dto.product.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getProductsByClass(String classCode);
}
