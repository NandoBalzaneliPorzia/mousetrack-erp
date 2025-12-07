package com.comexapp.DTO;

/*
Responsável: Nando Balzaneli
DTO usado ao enviar nova mensagem no chat.
Suporta:
- usuário autenticado: autorId + conteudo
- convidado: autorNome + conteudo
*/
public class NovaMensagemDTO {

    private Long autorId;
    private String autorNome;
    private String conteudo;

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

    public String getAutorNome() { return autorNome; }
    public void setAutorNome(String autorNome) { this.autorNome = autorNome; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
}
