package br.edu.iff.ccc.bsi.dpskt_api.entities;

import br.edu.iff.ccc.bsi.dpskt_api.enums.Corporation;
import br.edu.iff.ccc.bsi.dpskt_api.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

  @NotBlank(message = "The name cannot be blank")
  @Size(max = 50, message = "The player name must be at most 50 characters")
  @Column(nullable = false, length = 50)
  private String name;

  @Column(unique = true)
  private String playerId;

  @Enumerated(EnumType.STRING)
  private Corporation corporation = Corporation.DPSKT;

  @Enumerated(EnumType.STRING)
  private Role role = Role.RECRUTA;

  private Boolean statusClock = false;

  public Player(String name, String playerId, Role role, Corporation corporation) {
    this.name = name;
    this.playerId = playerId;
    this.role = role;
    this.corporation = corporation;
    this.statusClock = false;
  }

}
