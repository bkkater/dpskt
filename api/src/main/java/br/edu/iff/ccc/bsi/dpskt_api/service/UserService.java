package br.edu.iff.ccc.bsi.dpskt_api.service;

import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public Map<String, Object> createUser(User user) throws DataIntegrityViolationException {
        User savedUser = userRepository.save(user);
        Map<String, Object> response = new HashMap<>();
        response.put("response", savedUser);
        response.put("msg", "Registro criado com sucesso!");
        return response;
    }
    
    public Map<String, Object> getAllUsers() {
        List<User> users = userRepository.findAll();
        long entries = userRepository.count();
        long onlineClocks = userRepository.countByPlayerStatusClockTrue();
        
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("entries", entries);
        response.put("onlineClocks", onlineClocks);
        
        return response;
    }
    
    public Optional<User> getUserByDiscordId(String discordId) {
        return userRepository.findByDiscordId(discordId);
    }
    
    public Map<String, Object> deleteUserByPlayerId(String playerId) {
        Optional<User> user = userRepository.findByPlayerId(playerId);
        
        if (user.isEmpty()) {
            return null;
        }
        
        userRepository.delete(user.get());
        
        Map<String, Object> response = new HashMap<>();
        response.put("deleteService", user.get());
        response.put("msg", "Player excluido com sucesso");
        
        return response;
    }
    
    public Map<String, Object> updateUser(User updatedUser) {
        Optional<User> existingUser = userRepository.findByDiscordId(updatedUser.getDiscordId());
        
        if (existingUser.isEmpty()) {
            return null;
        }
        
        User user = existingUser.get();
        user.setDiscordId(updatedUser.getDiscordId());
        user.setPlayer(updatedUser.getPlayer());
        
        User updated = userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("update", updated);
        response.put("msg", "Player atualizado com sucesso!");
        
        return response;
    }
}