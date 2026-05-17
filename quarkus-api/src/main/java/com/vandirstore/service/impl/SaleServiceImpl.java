package com.vandirstore.service.impl;

import com.vandirstore.dto.SaleItemRequestDTO;
import com.vandirstore.dto.SaleRequestDTO;
import com.vandirstore.dto.SaleResponseDTO;
import com.vandirstore.model.Customer;
import com.vandirstore.model.Product;
import com.vandirstore.model.Sale;
import com.vandirstore.model.SaleDetail;
import com.vandirstore.model.User;
import com.vandirstore.model.enums.SaleStatus;
import com.vandirstore.repository.CustomerRepository;
import com.vandirstore.repository.ProductRepository;
import com.vandirstore.repository.SaleDetailRepository;
import com.vandirstore.repository.SaleRepository;
import com.vandirstore.repository.UserRepository;
import com.vandirstore.service.ISaleService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class SaleServiceImpl implements ISaleService {

    @Inject SaleRepository saleRepository;
    @Inject SaleDetailRepository saleDetailRepository;
    @Inject ProductRepository productRepository;
    @Inject UserRepository userRepository;
    @Inject CustomerRepository customerRepository;

    private SaleResponseDTO toDTO(Sale sale) {
        if (sale == null) return null;
        SaleResponseDTO dto = new SaleResponseDTO();
        dto.setId(sale.getId());
        dto.setTicketCode(sale.getTicketCode());
        if (sale.getCustomer() != null) {
            dto.setCustomerName(sale.getCustomer().getName());
        }
        dto.setSubtotal(sale.getSubtotal());
        dto.setTotalVat(sale.getTotalVat());
        dto.setTotalSale(sale.getTotalSale());
        dto.setStatus(sale.getStatus());
        dto.setSaleDate(sale.getSaleDate());
        return dto;
    }

    @Override
    public SaleResponseDTO findById(Integer id) {
        return toDTO(saleRepository.findById(id.longValue()));
    }

    @Override
    public SaleResponseDTO findByTicketCode(String code) {
        return toDTO(saleRepository.findByTicketCode(code));
    }

    @Override
    public List<SaleResponseDTO> listAllSales() {
        return saleRepository.listAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SaleResponseDTO> listSalesByDateRange(LocalDateTime start, LocalDateTime end) {
        return saleRepository.listByDateRange(start, end).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO saleDTO) {
        if (saleDTO.getItems() == null || saleDTO.getItems().isEmpty()) {
            throw new IllegalArgumentException("Sale must contain at least one item.");
        }

        User seller = userRepository.findById(saleDTO.getSellerId().longValue());
        if (seller == null) throw new IllegalArgumentException("Invalid seller.");

        Customer customer = null;
        if (saleDTO.getCustomerId() != null) {
            customer = customerRepository.findById(saleDTO.getCustomerId().longValue());
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        Sale sale = new Sale();
        sale.setTicketCode("TK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        sale.setSeller(seller);
        sale.setCustomer(customer);
        sale.setPaymentMethod(saleDTO.getPaymentMethod());
        sale.setStatus(SaleStatus.COMPLETED);

        // Pre-persist to get ID for details
        sale.setSubtotal(BigDecimal.ZERO);
        sale.setTotalVat(BigDecimal.ZERO);
        sale.setTotalSale(BigDecimal.ZERO);
        saleRepository.persist(sale);

        for (SaleItemRequestDTO itemDTO : saleDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId().longValue());
            if (product == null) throw new IllegalArgumentException("Product not found: " + itemDTO.getProductId());
            
            if (product.getCurrentStock() < itemDTO.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            // Deduct stock
            product.setCurrentStock(product.getCurrentStock() - itemDTO.getQuantity());

            SaleDetail detail = new SaleDetail();
            detail.setSale(sale);
            detail.setProduct(product);
            detail.setQuantity(itemDTO.getQuantity());
            detail.setAppliedPrice(product.getUnitPrice());
            
            BigDecimal itemSubtotal = product.getUnitPrice().multiply(new BigDecimal(itemDTO.getQuantity()));
            detail.setSubtotal(itemSubtotal);
            
            saleDetailRepository.persist(detail);
            
            subtotal = subtotal.add(itemSubtotal);
        }

        // Calculate final totals (Assuming 13% VAT based on DB script)
        BigDecimal vatRate = new BigDecimal("0.13");
        BigDecimal totalVat = subtotal.multiply(vatRate);
        BigDecimal totalSale = subtotal.add(totalVat);

        sale.setSubtotal(subtotal);
        sale.setTotalVat(totalVat);
        sale.setTotalSale(totalSale);

        return toDTO(sale);
    }

    @Override
    @Transactional
    public boolean cancelSale(Integer id) {
        Sale sale = saleRepository.findById(id.longValue());
        if (sale != null && sale.getStatus() == SaleStatus.COMPLETED) {
            sale.setStatus(SaleStatus.CANCELED);
            
            // Reverse stock
            List<SaleDetail> details = saleDetailRepository.listBySaleId(id);
            for (SaleDetail detail : details) {
                Product product = detail.getProduct();
                product.setCurrentStock(product.getCurrentStock() + detail.getQuantity());
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteSale(Integer id) {
        return saleRepository.deleteById(id.longValue());
    }
}
