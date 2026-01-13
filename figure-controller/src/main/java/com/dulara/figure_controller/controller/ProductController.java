package com.dulara.figure_controller.controller;

import com.dulara.figure_controller.dto.myFigure.MyGWPDetailedResponseDTO;
import com.dulara.figure_controller.dto.product.ProductResponseDTO;
import com.dulara.figure_controller.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/getProducts")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/productByClass" )
    public ResponseEntity<List<ProductResponseDTO>> getProductsByClass(@RequestParam String classCode) {
        List<ProductResponseDTO> response = productService.getProductsByClass(classCode);
        return ResponseEntity.ok(response);
    }
}
