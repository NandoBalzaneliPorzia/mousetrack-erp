package com.comexapp.DTO;

/*
A classe ChatMessageDTO.java representa um DTO - Objeto de Transferência de Dados
para mensagens de chat, encapsulando informações como ID, autor, conteúdo, data 
de envio e status de leitura.
*/

import java.time.LocalDateTime;

public class ChatMessageDTO {

    private Long id;
    private Long autorId;
    private String autorNome;
    private String conteudo;
    private LocalDateTime enviadoEm;
    private boolean lido;

    public ChatMessageDTO() {}

    public ChatMessageDTO(Long id, Long autorId, String autorNome,
                          String conteudo, LocalDateTime enviadoEm, boolean lido) {
        this.id = id;
        this.autorId = autorId;
        this.autorNome = autorNome;
        this.conteudo = conteudo;
        this.enviadoEm = enviadoEm;
        this.lido = lido;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

    public String getAutorNome() { return autorNome; }
    public void setAutorNome(String autorNome) { this.autorNome = autorNome; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getEnviadoEm() { return enviadoEm; }
    public void setEnviadoEm(LocalDateTime enviadoEm) { this.enviadoEm = enviadoEm; }

    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }
}
