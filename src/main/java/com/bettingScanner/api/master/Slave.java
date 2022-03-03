package com.bettingScanner.api.master;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Slaves")
public class Slave {
    @Id
    private String ipAddress;
    @Column(nullable = false)
    private Integer port;
    @Column(nullable = false)
    private Boolean enabled;
    private LocalDateTime lastUsed;

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getLastUsed() {
        return this.lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }
}
