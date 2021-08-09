package com.bettingScanner.api.requests;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RequestsRepository extends JpaRepository<Request, Integer> {
    public List<Request> findByFinnished(Boolean finnished);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Requests WHERE requestType='GENERATED'", nativeQuery = true)
    public void clearGenerated();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Requests SET state=''", nativeQuery = true)
    public void resetStates();
}
