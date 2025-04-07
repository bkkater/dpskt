package br.edu.iff.ccc.bsi.dpskt_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.iff.ccc.bsi.dpskt_api.entities.Clock;

public interface ClockRepository extends JpaRepository<Clock, UUID> {
    List<Clock> findByUser_DiscordId(String discordId);
}
