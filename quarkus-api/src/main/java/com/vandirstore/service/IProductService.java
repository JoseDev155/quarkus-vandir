package com.vandirstore.service;

import com.vandirstore.dto.ProductRequestDTO;
import com.vandirstore.dto.ProductResponseDTO;
import java.util.List;

public interface IProductService {
    ProductResponseDTO findById(Integer id);
    ProductResponseDTO findByCode(String code);
    List<ProductResponseDTO> listAllProducts();
    List<ProductResponseDTO> listLowStock();
    List<ProductResponseDTO> listByCategory(Integer categoryId);
    ProductResponseDTO createProduct(ProductRequestDTO productDTO);
    ProductResponseDTO updateProduct(Integer id, ProductRequestDTO productDTO);
    boolean changeStatus(Integer id, boolean active);
    boolean deleteProduct(Integer id);
}
