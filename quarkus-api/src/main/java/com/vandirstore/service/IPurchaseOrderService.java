package com.vandirstore.service;

import com.vandirstore.dto.PurchaseOrderRequestDTO;
import com.vandirstore.dto.PurchaseOrderResponseDTO;
import com.vandirstore.model.enums.PurchaseOrderStatus;
import java.util.List;

public interface IPurchaseOrderService {
    PurchaseOrderResponseDTO findById(Integer id);
    List<PurchaseOrderResponseDTO> listAllOrders();
    List<PurchaseOrderResponseDTO> listByStatus(PurchaseOrderStatus status);
    PurchaseOrderResponseDTO createOrder(PurchaseOrderRequestDTO orderDTO);
    PurchaseOrderResponseDTO updateOrderStatus(Integer id, PurchaseOrderStatus newStatus);
    boolean deleteOrder(Integer id);
}
