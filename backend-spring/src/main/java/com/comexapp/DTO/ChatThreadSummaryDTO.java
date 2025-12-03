package com.comexapp.DTO;

/*
A classe ChatThreadSummaryDTO.java representa um DTO para o resumo de 
um tópico de chat (thread). Contém informações essenciais como ID, 
título, última mensagem e o número de mensagens não lidas.
*/

public class ChatThreadSummaryDTO {

    private Long id;
    private String titulo;
    private String ultimaMensagem;
    private long naoLidas;

    public ChatThreadSummaryDTO() {}

    public ChatThreadSummaryDTO(Long id, String titulo, String ultimaMensagem, long naoLidas) {
        this.id = id;
        this.titulo = titulo;
        this.ultimaMensagem = ultimaMensagem;
        this.naoLidas = naoLidas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getUltimaMensagem() { return ultimaMensagem; }
    public void setUltimaMensagem(String ultimaMensagem) { this.ultimaMensagem = ultimaMensagem; }

    public long getNaoLidas() { return naoLidas; }
    public void setNaoLidas(long naoLidas) { this.naoLidas = naoLidas; }
}
