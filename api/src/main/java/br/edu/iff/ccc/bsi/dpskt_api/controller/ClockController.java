package br.edu.iff.ccc.bsi.dpskt_api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iff.ccc.bsi.dpskt_api.entities.Clock;
import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.exception.ClockNotFoundException;
import br.edu.iff.ccc.bsi.dpskt_api.exception.PendingClockExistsException;
import br.edu.iff.ccc.bsi.dpskt_api.exception.UserNotFoundException;
import br.edu.iff.ccc.bsi.dpskt_api.service.ClockService;
import br.edu.iff.ccc.bsi.dpskt_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clocks")
@Tag(name = "Clocks", description = "Operações relacionadas aos registros de pontos da DPSKT")
public class ClockController {

  @Autowired
  private ClockService clockService;

  @Autowired
  private UserService userService;

  @GetMapping("/")
  @Operation(summary = "Listar todos os registros de pontos", description = "Retorna uma lista de todos os registros de pontos do sistema")
  @ApiResponse(responseCode = "200", description = "Lista de registros de pontos retornada com sucesso")
  @ApiResponse(responseCode = "404", description = "Nenhum registro de ponto encontrado")
  public ResponseEntity<?> findAll() {
    var clocks = clockService.findAllClocks();

    if (clocks.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No clocks found");
    }

    return ResponseEntity.status(HttpStatus.OK).body(clocks);
  }

  @GetMapping("/user/{discordId}")
  @Operation(summary = "Buscar registros de pontos pelo ID do discord", description = "Retorna os registros de pontos de um usuário específico pelo ID do discord")
  @ApiResponse(responseCode = "200", description = "Registros de pontos encontrados com sucesso")
  @ApiResponse(responseCode = "404", description = "Nenhum registro de ponto encontrado para este usuário")
  public ResponseEntity<?> findByUser(String discordId) {
    var clocks = clockService.findClockByUser(discordId);

    if (clocks.isEmpty()) {
      throw new ClockNotFoundException("No clocks found for this user");
    }

    return ResponseEntity.status(HttpStatus.OK).body(clocks);
  }

  @PostMapping("/{discordId}")
  @Operation(summary = "Criar um novo registro de ponto", description = "Cria um novo registro de ponto para o usuário")
  @ApiResponse(responseCode = "201", description = "Registro de ponto criado com sucesso")
  @ApiResponse(responseCode = "400", description = "Erro ao criar o registro de ponto")
  public ResponseEntity<?> create(
      @Parameter(description = "ID do Discord do usuário") @PathVariable String discordId) {

    if (!userService.userExists(discordId)) {
      throw new UserNotFoundException("User not found");
    }

    User user = userService.findByDiscordId(discordId);

    Clock newClock = new Clock();
    newClock.setUser(user);

    if (clockService.hasPendingClock(discordId)) {
      throw new PendingClockExistsException("User has a pending clock");
    }

    var clockCreated = clockService.createClock(newClock);
    return ResponseEntity.status(HttpStatus.CREATED).body(clockCreated);
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Atualizar um registro de ponto", description = "Atualiza um registro de ponto existente")
  @ApiResponse(responseCode = "200", description = "Registro de ponto atualizado com sucesso")
  @ApiResponse(responseCode = "404", description = "Registro de ponto não encontrado")
  public ResponseEntity<?> patch(
      @Parameter(description = "ID do registro de ponto") @PathVariable UUID id,
      @Parameter(description = "Dados do registro de ponto") @Valid @RequestBody Clock clockModel) {

    if (!clockService.clockExists(id)) {
      throw new ClockNotFoundException("No clocks found");
    }

    var clockUpdated = clockService.patchClock(id, clockModel);
    return ResponseEntity.status(HttpStatus.OK).body(clockUpdated);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Deletar um registro de ponto", description = "Deleta um registro de ponto existente")
  @ApiResponse(responseCode = "200", description = "Registro de ponto deletado com sucesso")
  @ApiResponse(responseCode = "404", description = "Registro de ponto não encontrado")
  public ResponseEntity<?> delete(
      @Parameter(description = "ID do registro de ponto") @PathVariable UUID id) {

    var clockDeleted = clockService.deleteClock(id);

    if (clockDeleted == null) {
      throw new ClockNotFoundException("No clocks found");
    }

    return ResponseEntity.status(HttpStatus.OK).body(clockDeleted);
  }

}
