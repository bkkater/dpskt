package br.edu.iff.ccc.bsi.dpskt_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.iff.ccc.bsi.dpskt_api.entities.Player;
import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean userExists(String discordId) {
        return userRepository.findByDiscordId(discordId) != null;
    }

    public Optional<User> findByDiscordId(String discordId) {
        return userRepository.findByDiscordId(discordId);
    }

    public User createUser(User userModel) {
        return userRepository.save(userModel);
    }

    public User patch(String discordId, User userModel) {
        Optional<User> optionalUser = userRepository.findByDiscordId(discordId);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if (userModel.getIsAdmin() != null) {
            user.setIsAdmin(userModel.getIsAdmin());
        }

        var updatedPlayer = userModel.getPlayer();

        if (updatedPlayer != null) {
            Player player = user.getPlayer();

            if (updatedPlayer.getPlayerId() != null) {
                player.setPlayerId(updatedPlayer.getPlayerId());
            }

            if (updatedPlayer.getName() != null) {
                player.setName(updatedPlayer.getName());
            }

            if (updatedPlayer.getCorporation() != null) {
                player.setCorporation(updatedPlayer.getCorporation());
            }

            if (updatedPlayer.getRole() != null) {
                player.setRole(updatedPlayer.getRole());
            }

            if (updatedPlayer.getStatusClock() != null) {
                player.setStatusClock(updatedPlayer.getStatusClock());
            }
        }

        return userRepository.save(user);
    }

    public User deleteUser(String discordId) {
        Optional<User> optionalUser = userRepository.findByDiscordId(discordId);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        userRepository.delete(user);

        return user;
    }
}
