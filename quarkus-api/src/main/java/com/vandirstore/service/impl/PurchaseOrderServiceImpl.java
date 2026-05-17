package com.vandirstore.service.impl;

import com.vandirstore.dto.PurchaseOrderRequestDTO;
import com.vandirstore.dto.PurchaseOrderResponseDTO;
import com.vandirstore.model.Provider;
import com.vandirstore.model.PurchaseOrder;
import com.vandirstore.model.User;
import com.vandirstore.model.enums.PurchaseOrderStatus;
import com.vandirstore.repository.ProviderRepository;
import com.vandirstore.repository.PurchaseOrderRepository;
import com.vandirstore.repository.UserRepository;
import com.vandirstore.service.IPurchaseOrderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class PurchaseOrderServiceImpl implements IPurchaseOrderService {

    @Inject PurchaseOrderRepository orderRepository;
    @Inject ProviderRepository providerRepository;
    @Inject UserRepository userRepository;

    private PurchaseOrderResponseDTO toDTO(PurchaseOrder order) {
        if (order == null) return null;
        PurchaseOrderResponseDTO dto = new PurchaseOrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderCode(order.getOrderCode());
        if (order.getProvider() != null) dto.setProviderName(order.getProvider().getName());
        if (order.getManager() != null) dto.setManagerName(order.getManager().getName());
        dto.setEstimatedTotal(order.getEstimatedTotal());
        dto.setStatus(order.getStatus());
        dto.setRequestDate(order.getRequestDate());
        return dto;
    }

    @Override
    public PurchaseOrderResponseDTO findById(Integer id) {
        return toDTO(orderRepository.findById(id.longValue()));
    }

    @Override
    public List<PurchaseOrderResponseDTO> listAllOrders() {
        return orderRepository.listAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOrderResponseDTO> listByStatus(PurchaseOrderStatus status) {
        return orderRepository.listByStatus(status).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDTO createOrder(PurchaseOrderRequestDTO orderDTO) {
        Provider provider = providerRepository.findById(orderDTO.getProviderId().longValue());
        User manager = userRepository.findById(orderDTO.getManagerId().longValue());
        
        if (provider == null || manager == null) {
            throw new IllegalArgumentException("Invalid provider or manager ID");
        }

        PurchaseOrder order = new PurchaseOrder();
        order.setOrderCode("PO-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        order.setProvider(provider);
        order.setManager(manager);
        order.setEstimatedTotal(orderDTO.getEstimatedTotal());
        order.setStatus(PurchaseOrderStatus.DRAFT);
        
        orderRepository.persist(order);
        return toDTO(order);
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDTO updateOrderStatus(Integer id, PurchaseOrderStatus newStatus) {
        PurchaseOrder order = orderRepository.findById(id.longValue());
        if (order != null) {
            order.setStatus(newStatus);
            if (newStatus == PurchaseOrderStatus.RECEIVED) {
                order.setReceptionDate(LocalDateTime.now());
                // Logic to update inventory would be called here via another service call
            }
        }
        return toDTO(order);
    }

    @Override
    @Transactional
    public boolean deleteOrder(Integer id) {
        return orderRepository.deleteById(id.longValue());
    }
}
