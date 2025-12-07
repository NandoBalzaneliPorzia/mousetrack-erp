package com.comexapp.model;

/*
Responsável: Juliana Prado
A classe Cliente.java representa a entidade 'Cliente' no banco de dados.
Ela mapeia a tabela 'clientes' e define os atributos principais de um cliente:
- id: chave primária
- nome: nome do cliente (único e obrigatório)
- cnpj: número de identificação fiscal (opcional, com limite de 50 caracteres)
Inclui getters e setters para manipulação desses atributos.
*/

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(length = 50)
    private String cnpj;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
}
