package com.vandirstore;

import com.vandirstore.dto.SaleItemRequestDTO;
import com.vandirstore.dto.SaleRequestDTO;
import com.vandirstore.model.Product;
import com.vandirstore.model.User;
import com.vandirstore.model.enums.PaymentMethod;
import com.vandirstore.repository.CustomerRepository;
import com.vandirstore.repository.ProductRepository;
import com.vandirstore.repository.SaleDetailRepository;
import com.vandirstore.repository.SaleRepository;
import com.vandirstore.repository.UserRepository;
import com.vandirstore.service.impl.SaleServiceImpl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.util.Collections;

@QuarkusTest
public class SaleServiceImplTest {

    @Inject
    SaleServiceImpl saleService;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    ProductRepository productRepository;

    @InjectMock
    SaleRepository saleRepository;

    @InjectMock
    SaleDetailRepository saleDetailRepository;

    @InjectMock
    CustomerRepository customerRepository;

    @Test
    public void testCreateSaleThrowsExceptionWhenInsufficientStock() {
        // Arrange
        SaleRequestDTO request = new SaleRequestDTO();
        request.setSellerId(1);
        request.setPaymentMethod(PaymentMethod.CASH);
        
        SaleItemRequestDTO item = new SaleItemRequestDTO();
        item.setProductId(100);
        item.setQuantity(10); // Trying to buy 10
        request.setItems(Collections.singletonList(item));

        User mockSeller = new User();
        mockSeller.setId(1);
        
        Product mockProduct = new Product();
        mockProduct.setId(100);
        mockProduct.setName("Drill");
        mockProduct.setCurrentStock(5); // Only 5 in stock!
        mockProduct.setUnitPrice(new BigDecimal("100.00"));

        Mockito.when(userRepository.findById(1L)).thenReturn(mockSeller);
        Mockito.when(productRepository.findById(100L)).thenReturn(mockProduct);

        // Act & Assert
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            saleService.createSale(request);
        });

        Assertions.assertTrue(exception.getMessage().contains("Insufficient stock"));
    }
}
