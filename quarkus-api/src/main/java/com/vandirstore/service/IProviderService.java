package com.vandirstore.service;

import com.vandirstore.dto.ProviderDTO;
import java.util.List;

public interface IProviderService {
    ProviderDTO findById(Integer id);
    List<ProviderDTO> listAllProviders();
    ProviderDTO createProvider(ProviderDTO providerDTO);
    ProviderDTO updateProvider(Integer id, ProviderDTO providerDTO);
    boolean deleteProvider(Integer id);
}
