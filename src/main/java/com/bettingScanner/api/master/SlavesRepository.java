package com.bettingScanner.api.master;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlavesRepository extends JpaRepository<Slave, String> {
    public List<Slave> findAllByEnabled(Boolean enabled);
}
