package com.vandirstore.repository;

import com.vandirstore.model.BackupLog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BackupLogRepository implements PanacheRepository<BackupLog> {
    
    public BackupLog findLatestBackup() {
        return findAll(Sort.by("generatedAt").descending()).firstResult();
    }
}
