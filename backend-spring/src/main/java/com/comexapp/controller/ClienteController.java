package com.comexapp.controller;

import com.comexapp.model.Cliente;
import com.comexapp.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping({"/api/clientes", "/api/clientes/"})
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    // ping de diagnóstico (ficará em /api/clientes/ping)
    @GetMapping({"/ping", "/ping/"})
    public String pingClientes() {
        return "clientes ok";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente novo) {
        Cliente salvo = clienteRepository.save(novo);
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente dto) {
        return clienteRepository.findById(id)
                .map(c -> {
                    c.setNome(dto.getNome());
                    // ... outros campos
                    return ResponseEntity.ok(clienteRepository.save(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) return ResponseEntity.notFound().build();
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

