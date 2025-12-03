package com.comexapp.DTO;

/*
A classe AtualizarPerfilDTO.java é usada para encapsular os dados necessários
para a atualização do perfil de um usuário, incluindo telefone e senha.
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
