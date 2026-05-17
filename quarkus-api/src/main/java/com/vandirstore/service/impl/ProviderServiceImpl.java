package com.vandirstore.service.impl;

import com.vandirstore.dto.ProviderDTO;
import com.vandirstore.model.Provider;
import com.vandirstore.repository.ProviderRepository;
import com.vandirstore.service.IProviderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProviderServiceImpl implements IProviderService {

    @Inject
    ProviderRepository providerRepository;

    private ProviderDTO toDTO(Provider provider) {
        if (provider == null) return null;
        ProviderDTO dto = new ProviderDTO();
        dto.setId(provider.getId());
        dto.setName(provider.getName());
        dto.setContact(provider.getContact());
        dto.setPhone(provider.getPhone());
        dto.setEmail(provider.getEmail());
        return dto;
    }

    @Override
    public ProviderDTO findById(Integer id) {
        return toDTO(providerRepository.findById(id.longValue()));
    }

    @Override
    public List<ProviderDTO> listAllProviders() {
        return providerRepository.listAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProviderDTO createProvider(ProviderDTO providerDTO) {
        Provider provider = new Provider();
        provider.setName(providerDTO.getName());
        provider.setContact(providerDTO.getContact());
        provider.setPhone(providerDTO.getPhone());
        provider.setEmail(providerDTO.getEmail());
        
        providerRepository.persist(provider);
        return toDTO(provider);
    }

    @Override
    @Transactional
    public ProviderDTO updateProvider(Integer id, ProviderDTO providerDTO) {
        Provider existingProvider = providerRepository.findById(id.longValue());
        if (existingProvider != null) {
            existingProvider.setName(providerDTO.getName());
            existingProvider.setContact(providerDTO.getContact());
            existingProvider.setPhone(providerDTO.getPhone());
            existingProvider.setEmail(providerDTO.getEmail());
        }
        return toDTO(existingProvider);
    }

    @Override
    @Transactional
    public boolean deleteProvider(Integer id) {
        return providerRepository.deleteById(id.longValue());
    }
}
