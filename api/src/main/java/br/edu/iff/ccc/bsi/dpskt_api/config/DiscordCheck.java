package br.edu.iff.ccc.bsi.dpskt_api.config;

import br.edu.iff.ccc.bsi.dpskt_api.repository.UserRepository;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DiscordCheck extends OncePerRequestFilter {

  @Autowired
  private UserRepository userRepository;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()
        && authentication.getPrincipal() instanceof OAuth2User) {
      OAuth2User user = (OAuth2User) authentication.getPrincipal();
      String discordId = user.getAttribute("id");

      boolean userExists = userRepository.findByDiscordId(discordId).isPresent();

      if (!userExists
          && !(request.getRequestURI().startsWith("/register") || request.getRequestURI().equals("/user/register"))
          && !isPublicResource(request.getRequestURI())) {

        response.sendRedirect("/register");
        return;
      }

      if (userExists && request.getRequestURI().startsWith("/register")) {
        response.sendRedirect("/");
        return;
      }

    }

    filterChain.doFilter(request, response);
  }

  private boolean isPublicResource(String uri) {
    return uri.startsWith("/oauth2/") || uri.equals("/main.css") || uri.startsWith("/images/")
        || uri.startsWith("/icons/") || uri.startsWith("/h2-console/");
  }
}
