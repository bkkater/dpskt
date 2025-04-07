package br.edu.iff.ccc.bsi.dpskt_api.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity(name = "tb_users")
public class User {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(unique = true, nullable = false, name = "discord_id")
  @NotBlank(message = "The discordId cannot be blank")
  @Size(min = 18, max = 18, message = "The discordId must have 18 characters")
  private String discordId;

  private Boolean isAdmin = false;

  @Embedded
  private Player player;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
