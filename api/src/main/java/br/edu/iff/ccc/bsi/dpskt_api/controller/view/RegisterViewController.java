package br.edu.iff.ccc.bsi.dpskt_api.controller.view;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.iff.ccc.bsi.dpskt_api.dto.PlayerDTO;
import br.edu.iff.ccc.bsi.dpskt_api.dto.UserDTO;
import br.edu.iff.ccc.bsi.dpskt_api.entities.Player;
import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.enums.Corporation;
import br.edu.iff.ccc.bsi.dpskt_api.enums.Role;
import br.edu.iff.ccc.bsi.dpskt_api.service.UserService;

@Controller
public class RegisterViewController {

  @Autowired
  private UserService userService;

  @GetMapping("/register")
  public String getRegister(Model model, OAuth2AuthenticationToken authentication) {
    OAuth2User authUser = authentication.getPrincipal();
    String username = extractUsername(authUser);

    addBasicInfoToModel(model, username);
    addFormEnumsToModel(model);

    return "register";
  }

  @GetMapping("/user/register/success")
  public String getRegisterSuccess(Model model, OAuth2AuthenticationToken authentication) {
    OAuth2User authUser = authentication.getPrincipal();
    String username = extractUsername(authUser);

    model.addAttribute("username", username);

    return "register-success";
  }

  @PostMapping("/user/register")
  public String postRegister(
      @ModelAttribute UserDTO userDTO,
      OAuth2AuthenticationToken authentication) {

    OAuth2User authUser = authentication.getPrincipal();
    String discordId = extractDiscordId(authUser);

    User user = createOrUpdateUser(userDTO, discordId);
    userService.createUser(user);

    return "redirect:/user/register/success";
  }

  private String extractUsername(OAuth2User authUser) {
    return (String) authUser.getAttributes().get("username");
  }

  private String extractDiscordId(OAuth2User authUser) {
    return (String) authUser.getAttributes().get("id");
  }

  private void addBasicInfoToModel(Model model, String username) {
    model.addAttribute("username", username);
  }

  private void addFormEnumsToModel(Model model) {
    model.addAttribute("roles", Role.values());
    model.addAttribute("corporations", Corporation.values());
  }

  private Player createPlayerFromDTO(PlayerDTO dto) {
    return new Player(
        dto.getName(),
        dto.getPlayerId(),
        Role.valueOf(dto.getRole()),
        Corporation.valueOf(dto.getCorporation()));
  }

  private User createOrUpdateUser(UserDTO userDTO, String discordId) {
    PlayerDTO playerDTO = userDTO.getPlayer();
    Player player = createPlayerFromDTO(playerDTO);

    Optional<User> optionalUser = userService.findByDiscordId(discordId);
    User user = optionalUser.orElseGet(User::new);

    user.setDiscordId(discordId);
    user.setPlayer(player);

    return user;
  }

}