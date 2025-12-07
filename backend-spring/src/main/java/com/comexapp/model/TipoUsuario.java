package com.comexapp.model;

/*
Responsável: Juliana Prado
A enumeração TipoUsuario.java define os tipos de usuários disponíveis no sistema ComexApp.
Cada valor representa um papel específico com permissões e funcionalidades distintas:

- admin: usuário com privilégios administrativos completos.
- colaborador: usuário interno da empresa que realiza tarefas operacionais.
- agente: usuário que atua como agente de transporte ou logística.
- cliente_externo: usuário externo que acessa o sistema para acompanhar processos ou serviços.
*/
public enum TipoUsuario {
    admin,
    colaborador,
    agente,
    cliente_externo
}
