package com.bettingScanner.api.requests;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RequestsRepository extends JpaRepository<Request, Integer> {
    public List<Request> findByFinnished(Boolean finnished);

    public void deleteByRequestType(String requestType);

    @Modifying
    @Query(value = "UPDATE Requests SET state=''", nativeQuery = true)
    public void resetStates();
}
