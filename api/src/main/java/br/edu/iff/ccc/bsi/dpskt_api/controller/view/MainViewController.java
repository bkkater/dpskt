package br.edu.iff.ccc.bsi.dpskt_api.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "home") // URL
public class MainViewController {

    @GetMapping("/")  // GET http://localhost:8080/home/
    public String gethome() {
        return "home.html";
    }
}