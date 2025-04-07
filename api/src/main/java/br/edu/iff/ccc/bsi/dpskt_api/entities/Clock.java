package br.edu.iff.ccc.bsi.dpskt_api.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name = "tb_clocks")
public class Clock {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @CreationTimestamp
  private LocalDateTime startAt;

  private LocalDateTime endAt;

  @ManyToOne
  @JoinColumn(name = "user_discord_id", referencedColumnName = "discord_id")
  private User user;

}
