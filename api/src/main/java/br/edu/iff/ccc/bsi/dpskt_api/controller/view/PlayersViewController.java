package br.edu.iff.ccc.bsi.dpskt_api.controller.view;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.service.UserService;

@Controller
public class PlayersViewController {
  @Autowired
  private UserService userService;

  @GetMapping("/players")
  public String getPlayers(Model model, OAuth2AuthenticationToken authentication) {
    OAuth2User authUser = authentication.getPrincipal();
    String discordId = extractDiscordId(authUser);

    addUserInfoToModel(model, authUser, discordId);
    addPlayerInfoToModel(model, discordId);
    addNavigationTabsToModel(model);
    addPlayersDataToModel(model);

    return "players";
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

  private void addPlayersDataToModel(Model model) {
    List<User> users = userService.findAllUsers();
    model.addAttribute("users", users);
  }

  private void addNavigationTabsToModel(Model model) {
    List<Map<String, Object>> tabs = List.of(
        Map.of("title", "Home", "icon", "home", "url", "/", "active", false),
        Map.of("title", "Players", "icon", "users", "url", "/players", "active", true));

    model.addAttribute("tabs", tabs);
  }

}