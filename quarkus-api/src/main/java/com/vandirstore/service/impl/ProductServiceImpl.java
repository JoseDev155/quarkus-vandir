package com.vandirstore.service.impl;

import com.vandirstore.dto.ProductRequestDTO;
import com.vandirstore.dto.ProductResponseDTO;
import com.vandirstore.model.Category;
import com.vandirstore.model.Product;
import com.vandirstore.model.enums.ProductStatus;
import com.vandirstore.repository.CategoryRepository;
import com.vandirstore.repository.ProductRepository;
import com.vandirstore.service.IProductService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductServiceImpl implements IProductService {

    @Inject
    ProductRepository productRepository;
    
    @Inject
    CategoryRepository categoryRepository;

    private ProductResponseDTO toDTO(Product product) {
        if (product == null) return null;
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setCode(product.getCode());
        dto.setName(product.getName());
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
        }
        dto.setCurrentStock(product.getCurrentStock());
        dto.setMinStock(product.getMinStock());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setStatus(product.getStatus());
        return dto;
    }

    @Override
    public ProductResponseDTO findById(Integer id) {
        return toDTO(productRepository.findById(id.longValue()));
    }

    @Override
    public ProductResponseDTO findByCode(String code) {
        return toDTO(productRepository.findByCode(code));
    }

    @Override
    public List<ProductResponseDTO> listAllProducts() {
        return productRepository.listAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> listLowStock() {
        return productRepository.findLowStock().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> listByCategory(Integer categoryId) {
        return productRepository.listByCategory(categoryId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productDTO) {
        Product product = new Product();
        product.setCode(productDTO.getCode());
        product.setName(productDTO.getName());
        
        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId().longValue());
            product.setCategory(category);
        }
        
        product.setCurrentStock(0); // Default on creation
        product.setMinStock(productDTO.getMinStock());
        product.setUnitPrice(productDTO.getUnitPrice());
        product.setStatus(ProductStatus.ACTIVE);
        
        productRepository.persist(product);
        return toDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO productDTO) {
        Product existingProduct = productRepository.findById(id.longValue());
        if (existingProduct != null) {
            existingProduct.setName(productDTO.getName());
            
            if (productDTO.getCategoryId() != null) {
                Category category = categoryRepository.findById(productDTO.getCategoryId().longValue());
                existingProduct.setCategory(category);
            } else {
                existingProduct.setCategory(null);
            }
            
            existingProduct.setMinStock(productDTO.getMinStock());
            existingProduct.setUnitPrice(productDTO.getUnitPrice());
        }
        return toDTO(existingProduct);
    }

    @Override
    @Transactional
    public boolean changeStatus(Integer id, boolean active) {
        Product product = productRepository.findById(id.longValue());
        if (product != null) {
            product.setStatus(active ? ProductStatus.ACTIVE : ProductStatus.INACTIVE);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteProduct(Integer id) {
        return productRepository.deleteById(id.longValue());
    }
}
