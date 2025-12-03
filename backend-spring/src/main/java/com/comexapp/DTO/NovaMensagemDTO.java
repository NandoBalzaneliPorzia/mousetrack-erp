package com.comexapp.DTO;

/*
A classe NovaMensagemDTO.java para representar uma nova mensagem a ser enviada.
Contendo o ID do autor e o conteúdo da mensagem.
*/

public class NovaMensagemDTO {

    private Long autorId;
    private String conteudo;

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
}

