package com.bettingScanner.api.notifications;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatsRepository extends JpaRepository<ChatInfo, String> {

}
