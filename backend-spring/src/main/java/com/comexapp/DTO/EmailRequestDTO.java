package com.comexapp.DTO;

/*
A classe EmailRequestDTO.java representa um objeto de transferência de dados (DTO)
para encapsular as informações necessárias para o envio de um e-mail. Inclui campos 
para o destinatário ('para'), o assunto ('assunto')e o corpo da mensagem ('mensagem').
*/

public class EmailRequestDTO {
    private String para;
    private String assunto;
    private String mensagem;

    public String getPara() { return para; }
    public void setPara(String para) { this.para = para; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
