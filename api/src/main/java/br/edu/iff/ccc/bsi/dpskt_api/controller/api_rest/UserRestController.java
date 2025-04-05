package br.edu.iff.ccc.bsi.dpskt_api.controller.apirest;

import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        try {
            Map<String, Object> response = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg", "Id já cadastrado!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "Erro interno do servidor"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            Map<String, Object> response = userService.getAllUsers();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "Erro interno do servidor"));
        }
    }

    @GetMapping("/{discordId}")
    public ResponseEntity<?> get(@PathVariable String discordId) {
        try {
            Optional<User> user = userService.getUserByDiscordId(discordId);
            
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", "Player não encontrado!"));
            }
            
            return ResponseEntity.ok(user.get());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "Erro interno do servidor"));
        }
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<?> delete(@PathVariable String playerId) {
        try {
            Map<String, Object> response = userService.deleteUserByPlayerId(playerId);
            
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", "Player não encontrado!"));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "Erro interno do servidor"));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody User updatedUser) {
        try {
            Map<String, Object> response = userService.updateUser(updatedUser);
            
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", "Player não encontrado!"));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "Erro interno do servidor"));
        }
    }
}