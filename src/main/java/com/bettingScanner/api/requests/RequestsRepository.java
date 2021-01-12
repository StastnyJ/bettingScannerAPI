package com.bettingScanner.api.requests;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestsRepository extends JpaRepository<Request, Integer> {
    public List<Request> findByFinnished(Boolean finnished);
}
