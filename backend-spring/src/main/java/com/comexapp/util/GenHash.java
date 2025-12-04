package com.comexapp.util;

/*
A classe GenHash.java é uma utilidade simples para gerar hashes de senhas
usando BCrypt. Ela é usada principalmente para gerar a versão criptografada
de uma senha (ex: "123456") para testes ou inicialização de usuários.
*/

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenHash {
    public static void main(String[] args) {
        // Cria um codificador BCrypt com strength 10 (padrão)
        var enc = new BCryptPasswordEncoder(10);

        // Gera e imprime o hash da senha "123456"
        System.out.println(enc.encode("123456"));
    }
}
