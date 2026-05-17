package com.vandirstore.service;

import com.vandirstore.dto.SaleRequestDTO;
import com.vandirstore.dto.SaleResponseDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface ISaleService {
    SaleResponseDTO findById(Integer id);
    SaleResponseDTO findByTicketCode(String code);
    List<SaleResponseDTO> listAllSales();
    List<SaleResponseDTO> listSalesByDateRange(LocalDateTime start, LocalDateTime end);
    SaleResponseDTO createSale(SaleRequestDTO saleDTO);
    boolean cancelSale(Integer id);
    boolean deleteSale(Integer id);
}
