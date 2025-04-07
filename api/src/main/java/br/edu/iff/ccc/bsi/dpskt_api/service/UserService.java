package br.edu.iff.ccc.bsi.dpskt_api.service;

import java.util.List;

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

    public User findByDiscordId(String discordId) {
        return userRepository.findByDiscordId(discordId);
    }

    public User createUser(User userModel) {
        return userRepository.save(userModel);
    }

    public User patch(String discordId, User userModel) {
        User user = userRepository.findByDiscordId(discordId);

        if (userModel.getIsAdmin() != null) {
            user.setIsAdmin(userModel.getIsAdmin());
        }

        var updatedPlayer = userModel.getPlayer();

        if (updatedPlayer != null) {
            Player player = user.getPlayer();

            if (updatedPlayer.getPlayerId() != null) {
                player.setPlayerId(userModel.getPlayer().getPlayerId());
            }

            if (updatedPlayer.getName() != null) {
                player.setName(userModel.getPlayer().getName());
            }

            if (updatedPlayer.getCorporation() != null) {
                player.setCorporation(updatedPlayer.getCorporation());
            }

            if (updatedPlayer.getRole() != null) {
                player.setRole(userModel.getPlayer().getRole());
            }

            if (updatedPlayer.getStatusClock() != null) {
                player.setStatusClock(userModel.getPlayer().getStatusClock());
            }
        }

        return userRepository.save(user);
    }

    public User deleteUser(String discordId) {
        User user = userRepository.findByDiscordId(discordId);

        if (user == null) {
            return null;
        }

        userRepository.delete(user);

        return user;
    }
}
