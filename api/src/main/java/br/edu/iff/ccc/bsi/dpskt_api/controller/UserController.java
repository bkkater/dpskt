package br.edu.iff.ccc.bsi.dpskt_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.exception.UserAlreadyExistsException;
import br.edu.iff.ccc.bsi.dpskt_api.exception.UserNotFoundException;
import br.edu.iff.ccc.bsi.dpskt_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operações relacionadas aos usuários do DPSKT")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado")
    })
    public ResponseEntity<?> findAll() {
        var users = userService.findAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{discordId}")
    @Operation(summary = "Buscar usuário pelo ID do discord", description = "Retorna um usuário específico pelo ID do discord")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<?> findById(
            @PathVariable @Parameter(description = "ID do discord") String discordId) {

        if (!userService.userExists(discordId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var user = userService.findByDiscordId(discordId);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/")
    @Operation(summary = "Criar novo usuário", description = "Registra um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Usuário já existe ou dados inválidos")
    })
    public ResponseEntity<?> create(
            @Parameter(description = "Dados do novo usuário") @Valid @RequestBody User userModel) {

        if (userService.userExists(userModel.getDiscordId())) {
            throw new UserAlreadyExistsException("Usuário já existe com ID do discord: " + userModel.getDiscordId());
        }

        var userCreated = userService.createUser(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PatchMapping("/{discordId}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<?> update(
            @PathVariable @Parameter(description = "ID do discord") String discordId,
            @Parameter(description = "Dados do usuário a serem atualizados") @Valid @RequestBody User userModel) {

        if (!userService.userExists(userModel.getDiscordId())) {
            throw new UserNotFoundException("Usuário não encontrado com ID do discord: " + discordId);
        }

        var updatedUser = userService.patch(discordId, userModel);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{discordId}")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<?> delete(
            @PathVariable @Parameter(description = "ID do discord") String discordId) {

        if (!userService.userExists(discordId)) {
            throw new UserNotFoundException("Usuário não encontrado com ID do discord: " + discordId);
        }

        var deletedUser = userService.deleteUser(discordId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedUser);
    }

}
