package com.vandirstore.repository;

import com.vandirstore.model.Provider;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProviderRepository implements PanacheRepository<Provider> {
}
