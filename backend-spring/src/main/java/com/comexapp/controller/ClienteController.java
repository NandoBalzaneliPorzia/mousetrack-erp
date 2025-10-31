package com.comexapp.controller;

import com.comexapp.model.Cliente;
import com.comexapp.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")          // <<< classe mapeia só /api
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping({"/clientes", "/clientes/"})   // <<< método mapeia /clientes e /clientes/
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    // ping de diagnóstico (opcional)
    @GetMapping({"/clientes/ping", "/clientes/ping/"})
    public String pingClientes() {
        return "clientes ok";
    }
}
