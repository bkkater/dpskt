package br.edu.iff.ccc.bsi.dpskt_api.controller.api_rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "MainRest", description= "Exemplo de um Rest Controller")
public class MainRestController {
    @Operation(summary = "Retorna todos os exemplos armazenados")
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(path = "/exemplos")
    public ResponseEntity<List<Map<String, String>>> getExemplo() {
        List<Map<String, String>> lista = new ArrayList();
        Map<String, String> item1 = new HashMap<>();
        item1.put("id", "1");
        item1.put("nome", "Gabriel");

        Map<String, String> item2 = new HashMap<>();
        item2.put("id", "2");
        item2.put("nome", "Livia");


        lista.add(item1);
        lista.add(item2);
        return ResponseEntity.ok(lista);
    }
}
