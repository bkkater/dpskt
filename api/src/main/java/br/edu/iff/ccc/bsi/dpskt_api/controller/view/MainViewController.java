package br.edu.iff.ccc.bsi.dpskt_api.controller.view;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.entities.Clock;
import br.edu.iff.ccc.bsi.dpskt_api.service.ClockService;
import br.edu.iff.ccc.bsi.dpskt_api.service.UserService;

@Controller
public class MainViewController {
  @Autowired
  private UserService userService;

  @Autowired
  private ClockService clockService;

  @GetMapping("/")
  public String getHome(Model model, OAuth2AuthenticationToken authentication) {
    OAuth2User authUser = authentication.getPrincipal();
    String discordId = extractDiscordId(authUser);

    addUserInfoToModel(model, authUser, discordId);
    addPlayerInfoToModel(model, discordId);
    addNavigationTabsToModel(model);
    addClocksToModel(model, discordId);

    return "index";
  }

  @PostMapping("/clock/register")
  public RedirectView postRegister(OAuth2AuthenticationToken authentication) {
    OAuth2User authUser = authentication.getPrincipal();
    String discordId = extractDiscordId(authUser);
    User user = userService.findByDiscordId(discordId).orElseThrow();

    if (clockService.hasPendingClock(discordId)) {
      closeExistingClock(discordId);
      updatePlayerClockStatus(user, false);
    } else {
      createNewClock(user);
      updatePlayerClockStatus(user, true);
    }

    return new RedirectView("/?success=true");
  }

  @DeleteMapping("/clock/delete/{id}")
  public RedirectView deleteClock(@PathVariable UUID id, OAuth2AuthenticationToken authentication) {
    OAuth2User authUser = authentication.getPrincipal();
    String discordId = extractDiscordId(authUser);
    User user = userService.findByDiscordId(discordId).orElseThrow();

    if (clockService.hasPendingClock(discordId)) {
      closeExistingClock(discordId);
      updatePlayerClockStatus(user, false);
    }

    clockService.deleteClock(id);

    return new RedirectView("/?success=true");
  }

  private String extractDiscordId(OAuth2User authUser) {
    return (String) authUser.getAttributes().get("id");
  }

  private void addUserInfoToModel(Model model, OAuth2User authUser, String discordId) {
    String username = (String) authUser.getAttributes().get("username");
    String avatarId = (String) authUser.getAttributes().get("avatar");
    String avatarUrl = "https://cdn.discordapp.com/avatars/" + discordId + "/" + avatarId;

    model.addAttribute("username", username);
    model.addAttribute("avatar", avatarUrl);
  }

  private void addPlayerInfoToModel(Model model, String discordId) {
    Optional<User> user = userService.findByDiscordId(discordId);

    if (user.isPresent()) {
      model.addAttribute("player", user.get().getPlayer());
    } else {
      model.addAttribute("player", null);
    }
  }

  private void addClocksToModel(Model model, String discordId) {
    Optional<User> user = userService.findByDiscordId(discordId);

    if (user.isPresent()) {
      var clocks = clockService.findClockByUser(discordId);
      model.addAttribute("clocks", clocks);
    } else {
      model.addAttribute("clocks", null);
    }
  }

  private void addNavigationTabsToModel(Model model) {
    List<Map<String, Object>> tabs = List.of(
        Map.of("title", "Home", "icon", "home", "url", "/", "active", true),
        Map.of("title", "Players", "icon", "users", "url", "/players", "active", false));

    model.addAttribute("tabs", tabs);
  }

  private void closeExistingClock(String discordId) {
    Optional<Clock> openClock = clockService.findLastPendingClock(discordId);

    openClock.ifPresent(clock -> {
      clockService.patchClock(clock.getId(), LocalDateTime.now());
    });
  }

  private void createNewClock(User user) {
    Clock newClock = new Clock();
    newClock.setUser(user);
    clockService.createClock(newClock);
  }

  private void updatePlayerClockStatus(User user, boolean status) {
    user.getPlayer().setStatusClock(status);
    userService.patch(user.getDiscordId(), user);
  }
}