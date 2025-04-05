package br.edu.iff.ccc.bsi.dpskt_api.entities;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Player {
    private String id;
    private String name;
    private String corporation;
    private String role;
    private Boolean isAdmin;
    private Boolean statusClock;
    private LocalDateTime joinedAt;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCorporation() {
        return corporation;
    }
    
    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Boolean getIsAdmin() {
        return isAdmin;
    }
    
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public Boolean getStatusClock() {
        return statusClock;
    }
    
    public void setStatusClock(Boolean statusClock) {
        this.statusClock = statusClock;
    }
    
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
    
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}