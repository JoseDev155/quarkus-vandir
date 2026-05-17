package com.vandirstore.service;

import com.vandirstore.dto.ConfigurationDTO;
import com.vandirstore.dto.BackupLogResponseDTO;
import java.util.List;

public interface ISystemConfigService {
    ConfigurationDTO getGlobalConfig();
    ConfigurationDTO updateGlobalConfig(ConfigurationDTO configDTO);
    BackupLogResponseDTO generateManualBackup(Integer userId);
    List<BackupLogResponseDTO> listBackupHistory();
}
