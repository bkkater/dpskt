package br.edu.iff.ccc.bsi.dpskt_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.iff.ccc.bsi.dpskt_api.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByDiscordId(String discordId);

}
