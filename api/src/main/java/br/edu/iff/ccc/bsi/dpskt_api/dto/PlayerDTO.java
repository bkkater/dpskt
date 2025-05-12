package br.edu.iff.ccc.bsi.dpskt_api.dto;

import lombok.Data;

@Data
public class PlayerDTO {
  private String name;
  private String playerId;
  private String role;
  private String corporation;
}