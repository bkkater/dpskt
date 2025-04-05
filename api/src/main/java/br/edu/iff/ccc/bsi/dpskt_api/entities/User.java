package br.edu.iff.ccc.bsi.dpskt_api.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String discordId;
    
    @Embedded
    private Player player;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDiscordId() {
        return discordId;
    }
    
    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
}