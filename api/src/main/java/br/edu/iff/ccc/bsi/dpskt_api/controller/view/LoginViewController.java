package br.edu.iff.ccc.bsi.dpskt_api.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

  @GetMapping("/login")
  public String getLogin() {
    return "login";
  }

}
