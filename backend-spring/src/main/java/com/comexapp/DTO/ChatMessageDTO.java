package com.comexapp.DTO;

/*
A classe ChatMessageDTO.java é um DTO (Data Transfer Object) usado para transferir
informações de mensagens de chat entre o backend e o frontend. 
Ela encapsula os dados essenciais da mensagem, como:
- id: identificador único da mensagem
- autorId: ID do usuário que enviou a mensagem
- autorNome: nome do usuário autor
- conteudo: texto da mensagem
- enviadoEm: data e hora do envio
- lido: status indicando se a mensagem foi lida
*/
import java.time.LocalDateTime;

public class ChatMessageDTO {

    private Long id;
    private Long autorId;
    private String autorNome;
    private String conteudo;
    private LocalDateTime enviadoEm;
    private boolean lido;

    public ChatMessageDTO() {}

    public ChatMessageDTO(Long id, Long autorId, String autorNome,
                          String conteudo, LocalDateTime enviadoEm, boolean lido) {
        this.id = id;
        this.autorId = autorId;
        this.autorNome = autorNome;
        this.conteudo = conteudo;
        this.enviadoEm = enviadoEm;
        this.lido = lido;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

    public String getAutorNome() { return autorNome; }
    public void setAutorNome(String autorNome) { this.autorNome = autorNome; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getEnviadoEm() { return enviadoEm; }
    public void setEnviadoEm(LocalDateTime enviadoEm) { this.enviadoEm = enviadoEm; }

    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }
}
