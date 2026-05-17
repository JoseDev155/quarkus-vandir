package com.vandirstore.repository;

import com.vandirstore.model.Configuration;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigurationRepository implements PanacheRepository<Configuration> {
    
    public Configuration getGlobalConfig() {
        return findAll().firstResult();
    }
}
