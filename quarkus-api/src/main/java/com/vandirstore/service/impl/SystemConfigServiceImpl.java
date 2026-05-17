package com.vandirstore.service.impl;

import com.vandirstore.dto.ConfigurationDTO;
import com.vandirstore.dto.BackupLogResponseDTO;
import com.vandirstore.model.Configuration;
import com.vandirstore.model.BackupLog;
import com.vandirstore.repository.ConfigurationRepository;
import com.vandirstore.repository.BackupLogRepository;
import com.vandirstore.service.ISystemConfigService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SystemConfigServiceImpl implements ISystemConfigService {

    @Inject ConfigurationRepository configRepository;
    @Inject BackupLogRepository backupRepository;

    private ConfigurationDTO toConfigDTO(Configuration config) {
        if (config == null) return null;
        ConfigurationDTO dto = new ConfigurationDTO();
        dto.setCompanyName(config.getCompanyName());
        dto.setContactPhone(config.getContactPhone());
        dto.setVatTax(config.getVatTax());
        dto.setCurrency(config.getCurrency());
        return dto;
    }

    private BackupLogResponseDTO toBackupDTO(BackupLog log) {
        if (log == null) return null;
        BackupLogResponseDTO dto = new BackupLogResponseDTO();
        dto.setId(log.getId());
        dto.setFileName(log.getFileName());
        dto.setGeneratedAt(log.getGeneratedAt());
        dto.setSizeMb(log.getSizeMb());
        dto.setType(log.getType());
        if (log.getGeneratedBy() != null) {
            dto.setGeneratedByName(log.getGeneratedBy().getName());
        }
        return dto;
    }

    @Override
    public ConfigurationDTO getGlobalConfig() {
        return toConfigDTO(configRepository.getGlobalConfig());
    }

    @Override
    @Transactional
    public ConfigurationDTO updateGlobalConfig(ConfigurationDTO configDTO) {
        Configuration existing = configRepository.getGlobalConfig();
        if (existing != null) {
            existing.setCompanyName(configDTO.getCompanyName());
            existing.setContactPhone(configDTO.getContactPhone());
            existing.setVatTax(configDTO.getVatTax());
            existing.setCurrency(configDTO.getCurrency());
            return toConfigDTO(existing);
        }
        return null;
    }

    @Override
    @Transactional
    public BackupLogResponseDTO generateManualBackup(Integer userId) {
        // Here, a shell command or DB export utility would be executed.
        return null; // Logic pending system implementation
    }

    @Override
    public List<BackupLogResponseDTO> listBackupHistory() {
        return backupRepository.listAll().stream().map(this::toBackupDTO).collect(Collectors.toList());
    }
}
