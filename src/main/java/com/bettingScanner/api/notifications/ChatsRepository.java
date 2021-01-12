package com.bettingScanner.api.notifications;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatsRepository extends JpaRepository<ChatInfo, String> {
    public List<ChatInfo> findByVisible(Boolean visible);
}
