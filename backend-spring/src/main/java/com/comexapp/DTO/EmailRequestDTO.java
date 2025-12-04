package com.comexapp.DTO;

/*
A classe EmailRequestDTO.java é um DTO (Data Transfer Object) utilizado para 
representar os dados necessários ao envio de um e-mail. Contém os campos:
- para: endereço de e-mail do destinatário
- assunto: assunto do e-mail
- mensagem: corpo do e-mail
Esses dados são enviados do frontend para o backend via requisição HTTP.
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
