package br.edu.iff.ccc.bsi.dpskt_api.controller.api_rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.iff.ccc.bsi.dpskt_api.entities.Clock;
import br.edu.iff.ccc.bsi.dpskt_api.exception.ClockNotFoundException;
import br.edu.iff.ccc.bsi.dpskt_api.exception.PendingClockExistsException;
import br.edu.iff.ccc.bsi.dpskt_api.exception.UserNotFoundException;
import br.edu.iff.ccc.bsi.dpskt_api.service.ClockService;
import br.edu.iff.ccc.bsi.dpskt_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
  @Operation(summary = "Listar todos os registros de pontos")
  @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
  public ResponseEntity<?> findAll() {
    var clocks = clockService.findAllClocks();
    if (clocks.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No clocks found");
    }
    return ResponseEntity.ok(clocks);
  }

  @GetMapping("/user/{discordId}")
  @Operation(summary = "Buscar registros pelo ID do Discord")
  @ApiResponse(responseCode = "200", description = "Registros encontrados")
  @ApiResponse(responseCode = "404", description = "Nenhum registro encontrado")
  public ResponseEntity<?> findByUser(@PathVariable String discordId) {
    var clocks = clockService.findClockByUser(discordId);
    if (clocks.isEmpty()) {
      throw new ClockNotFoundException("No clocks found for this user");
    }
    return ResponseEntity.ok(clocks);
  }

  @PostMapping("/{discordId}")
  @Operation(summary = "Criar um novo registro de ponto")
  @ApiResponse(responseCode = "201", description = "Registro criado com sucesso")
  public ResponseEntity<?> create(
      @Parameter(description = "ID do Discord") @PathVariable String discordId) {

    var userOpt = userService.findByDiscordId(discordId);
    if (userOpt.isEmpty()) {
      throw new UserNotFoundException("User not found");
    }

    if (clockService.hasPendingClock(discordId)) {
      throw new PendingClockExistsException("User has a pending clock");
    }

    Clock newClock = new Clock();
    newClock.setUser(userOpt.get());

    var created = clockService.createClock(newClock);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Atualizar um registro de ponto")
  @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso")
  @ApiResponse(responseCode = "404", description = "Registro não encontrado")
  public ResponseEntity<?> patch(
      @Parameter(description = "ID do ponto") @PathVariable UUID id,
      @Valid @RequestBody Clock clockModel) {

    if (!clockService.clockExists(id)) {
      throw new ClockNotFoundException("No clocks found");
    }

    var updated = clockService.patchClock(id, clockModel);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Deletar um registro de ponto")
  @ApiResponse(responseCode = "200", description = "Registro deletado")
  @ApiResponse(responseCode = "404", description = "Registro não encontrado")
  public ResponseEntity<?> delete(@PathVariable UUID id) {
    var deleted = clockService.deleteClock(id);
    if (deleted == null) {
      throw new ClockNotFoundException("No clocks found");
    }
    return ResponseEntity.ok(deleted);
  }
}
