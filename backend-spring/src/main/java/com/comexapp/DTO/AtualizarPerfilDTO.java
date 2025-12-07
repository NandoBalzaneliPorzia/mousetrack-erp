package com.comexapp.DTO;

/*
Responsável: Laura Pereira
A classe AtualizarPerfilDTO.java é um objeto de transferência de dados (DTO)
utilizado para receber informações de atualização do perfil do usuário. 
Contém campos para telefone, nova senha e confirmação da senha, garantindo 
que os dados do front-end sejam encapsulados de forma segura antes de serem
processados pelo backend.
*/

public class AtualizarPerfilDTO {
    private String telefone;
    private String novaSenha;
    private String confirmarSenha;

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getNovaSenha() { return novaSenha; }
    public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }

    public String getConfirmarSenha() { return confirmarSenha; }
    public void setConfirmarSenha(String confirmarSenha) { this.confirmarSenha = confirmarSenha; }
}
