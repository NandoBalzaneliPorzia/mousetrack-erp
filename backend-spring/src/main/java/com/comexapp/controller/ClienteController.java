package com.comexapp.controller;

/*
Responsável: Ana Beatriz Maranho
A classe ClienteController.java é um controlador REST responsável por gerenciar
as operações CRUD (Create, Read, Update, Delete) da entidade Cliente.

Funcionalidades principais:
- Listar todos os clientes
- Buscar um cliente por ID
- Criar um novo cliente
- Atualizar um cliente existente
- Deletar um cliente
- Endpoint de diagnóstico (ping) para verificar se o serviço está ativo
*/

import com.comexapp.model.Cliente;
import com.comexapp.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping({"/api/clientes", "/api/clientes/"})
public class ClienteController {

    // Injeção do repositório que acessa os dados de Cliente
    @Autowired
    private ClienteRepository clienteRepository;

    // GET /api/clientes
    // Retorna a lista completa de clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    // GET /api/clientes/ping
    // Endpoint simples para testes e diagnóstico
    @GetMapping({"/ping", "/ping/"})
    public String pingClientes() {
        return "clientes ok";
    }

    // GET /api/clientes/{id}
    // Busca um cliente específico pelo ID
    // Retorna 404 se o cliente não for encontrado
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/clientes
    // Cria um novo cliente no banco de dados
    // Retorna o cliente recém-criado
    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente novo) {
        Cliente salvo = clienteRepository.save(novo);
        return ResponseEntity.ok(salvo);
    }

    // PUT /api/clientes/{id}
    // Atualiza os dados de um cliente existente
    // Retorna 404 se o cliente não for encontrado
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente dto) {
        return clienteRepository.findById(id)
                .map(c -> {
                    c.setNome(dto.getNome());
                    // ... outros campos podem ser atualizados aqui
                    return ResponseEntity.ok(clienteRepository.save(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/clientes/{id}
    // Deleta um cliente pelo ID
    // Retorna 404 se o cliente não existir
    // Retorna 204 No Content em caso de sucesso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) return ResponseEntity.notFound().build();
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
