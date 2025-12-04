package com.comexapp.DTO;

/*
A classe NovaMensagemDTO.java é um DTO (Data Transfer Object) usado para 
representar os dados de uma nova mensagem de chat que será enviada. 
Contém:
- autorId: ID do usuário que está enviando a mensagem
- conteudo: texto da mensagem
Esse DTO é enviado do frontend para o backend ao criar uma nova mensagem.
*/
public class NovaMensagemDTO {

    private Long autorId;
    private String conteudo;

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
}
