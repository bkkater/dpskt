package br.edu.iff.ccc.bsi.dpskt_api.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import br.edu.iff.ccc.bsi.dpskt_api.entities.User;
import br.edu.iff.ccc.bsi.dpskt_api.repository.UserRepository;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
    String discordId = oauth2User.getAttribute("id");

    Optional<User> userOptional = userRepository.findByDiscordId(discordId);

    if (userOptional.isEmpty()) {
      response.sendRedirect("/register");
      return;
    }

    response.sendRedirect("/");
  }
}