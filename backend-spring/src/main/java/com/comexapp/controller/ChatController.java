package com.comexapp.controller;

/*
A classe ChatController.java é um controlador REST responsável por gerenciar
as funcionalidades de chat da aplicação.

Funcionalidades principais:
- Listar todas as threads de chat (para exibição na coluna de conversas)
- Listar mensagens de uma thread específica, marcando-as como lidas
- Enviar nova mensagem em uma thread
- Criar nova thread de chat
- Criar ou obter automaticamente a thread de chat associada a um processo
*/

import com.comexapp.DTO.ChatThreadSummaryDTO;
import com.comexapp.DTO.ChatMessageDTO;
import com.comexapp.DTO.NovaMensagemDTO;
import com.comexapp.model.ChatThread;
import com.comexapp.model.Processo;
import com.comexapp.model.ChatMessage;
import com.comexapp.model.Usuario;
import com.comexapp.repository.ChatThreadRepository;
import com.comexapp.repository.ChatMessageRepository;
import com.comexapp.repository.UsuarioRepository;
import com.comexapp.repository.ProcessoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    // Repositórios para acessar threads, mensagens, usuários e processos
    private final ChatThreadRepository threadRepo;
    private final ChatMessageRepository messageRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProcessoRepository processoRepository;

    // Construtor: injeta os repositórios
    public ChatController(ChatThreadRepository threadRepo,
                          ChatMessageRepository messageRepo,
                          UsuarioRepository usuarioRepo,
                          ProcessoRepository processoRepository) {
        this.threadRepo = threadRepo;
        this.messageRepo = messageRepo;
        this.usuarioRepo = usuarioRepo;
        this.processoRepository = processoRepository;
    }

    // GET /threads
    // Lista todas as threads de chat para exibição na coluna da esquerda
    // Inclui pré-visualização da última mensagem e contagem de mensagens não lidas
    @GetMapping("/threads")
    public List<ChatThreadSummaryDTO> listarThreads() {
        return threadRepo.findAll().stream().map(thread -> {
            ChatMessage last = messageRepo.findFirstByThreadIdOrderByEnviadoEmDesc(thread.getId());
            long unread = messageRepo.countByThreadIdAndLidoFalse(thread.getId());
            String preview = (last != null ? last.getConteudo() : "");
            return new ChatThreadSummaryDTO(
                    thread.getId(),
                    thread.getTitulo(),
                    preview,
                    unread
            );
        }).collect(Collectors.toList());
    }

    // GET /threads/{id}/messages
    // Lista todas as mensagens de uma thread específica
    // Marca mensagens como lidas ao retornar
    @GetMapping("/threads/{id}/messages")
    public ResponseEntity<List<ChatMessageDTO>> listarMensagens(@PathVariable Long id) {
        if (!threadRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<ChatMessage> mensagens = messageRepo.findByThreadIdOrderByEnviadoEmAsc(id);

        // Marca todas as mensagens como lidas
        mensagens.forEach(m -> m.setLido(true));
        messageRepo.saveAll(mensagens);

        List<ChatMessageDTO> dto = mensagens.stream()
                .map(this::toMessageDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    // POST /threads/{id}/messages
    // Envia uma nova mensagem em uma thread existente
    @PostMapping("/threads/{id}/messages")
    public ResponseEntity<ChatMessageDTO> enviarMensagem(
            @PathVariable Long id,
            @RequestBody NovaMensagemDTO body) {

        ChatThread thread = threadRepo.findById(id).orElse(null);
        if (thread == null) return ResponseEntity.notFound().build();

        ChatMessage msg = new ChatMessage();
        msg.setThread(thread);

        if (body.getAutorId() != null) {
            Usuario autor = usuarioRepo.findById(body.getAutorId()).orElse(null);
            if (autor == null) return ResponseEntity.badRequest().build();
            msg.setAutor(autor);
        } else {
            msg.setAutor(null);
            msg.setAutorGuest(body.getAutorNome() != null ? body.getAutorNome() : "Convidado");
        }

        msg.setConteudo(body.getConteudo());

        ChatMessage salvo = messageRepo.save(msg);
        return ResponseEntity.ok(toMessageDTO(salvo));
    }

    // POST /threads
    // Cria uma nova thread de chat manualmente
    @PostMapping("/threads")
    public ResponseEntity<ChatThread> criarThread(@RequestBody ChatThread thread) {
        if (thread.getTitulo() == null || thread.getTitulo().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        ChatThread salvo = threadRepo.save(thread);
        return ResponseEntity.ok(salvo);
    }

    // Converte entidade ChatMessage em DTO para envio via API
    private ChatMessageDTO toMessageDTO(ChatMessage m) {
        Long autorId = (m.getAutor() != null ? m.getAutor().getId() : null);
        String nome = (m.getAutor() != null ? m.getAutor().getNome() : m.getAutorGuest());

        return new ChatMessageDTO(
                m.getId(),
                autorId,
                nome,
                m.getConteudo(),
                m.getEnviadoEm(),
                m.isLido()
        );
    }

    // POST /threads/processo/{processoId}
    // Cria ou retorna a thread de chat associada a um processo
    // Se a thread já existir para o processo, apenas retorna ela
    // Se não existir, cria uma nova thread com título baseado no código do processo
    @PostMapping("/threads/processo/{processoId}")
    public ResponseEntity<ChatThread> criarOuObterThreadDoProcesso(@PathVariable Long processoId) {

        Processo processo = processoRepository.findById(processoId).orElse(null);
        if (processo == null) {
            return ResponseEntity.badRequest().build();
        }

        ChatThread existente = threadRepo.findFirstByProcessoId(processoId);
        if (existente != null) {
            return ResponseEntity.ok(existente);
        }

        ChatThread novo = new ChatThread();
        novo.setProcesso(processo);
        novo.setTitulo(processo.getCodigo() + " - " + processo.getTitulo());

        ChatThread salvo = threadRepo.save(novo);

        // Mensagem automática
        ChatMessage msg = new ChatMessage();
        msg.setThread(salvo);
        msg.setConteudo("Olá! Nossa equipe já pode ver suas mensagens.");
        msg.setAutorGuest("Sistema");
        messageRepo.save(msg);

        return ResponseEntity.ok(salvo);
    }
}
