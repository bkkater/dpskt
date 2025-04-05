package br.edu.iff.ccc.bsi.dpskt_api.repository;

import br.edu.iff.ccc.bsi.webdev.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByDiscordId(String discordId);
    Optional<User> findByPlayerId(String playerId);
    long countByPlayerStatusClockTrue();
}