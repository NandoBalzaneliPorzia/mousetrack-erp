package com.comexapp.controller;

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

    private final ChatThreadRepository threadRepo;
    private final ChatMessageRepository messageRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProcessoRepository processoRepository;

    public ChatController(ChatThreadRepository threadRepo,
                          ChatMessageRepository messageRepo,
                          UsuarioRepository usuarioRepo,
                          ProcessoRepository processoRepository) {
        this.threadRepo = threadRepo;
        this.messageRepo = messageRepo;
        this.usuarioRepo = usuarioRepo;
        this.processoRepository = processoRepository;
    }

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

    @GetMapping("/threads/{id}/messages")
    public ResponseEntity<List<ChatMessageDTO>> listarMensagens(@PathVariable Long id) {
        if (!threadRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<ChatMessage> mensagens = messageRepo.findByThreadIdOrderByEnviadoEmAsc(id);
        mensagens.forEach(m -> m.setLido(true));
        messageRepo.saveAll(mensagens);

        return ResponseEntity.ok(
                mensagens.stream().map(this::toMessageDTO).collect(Collectors.toList())
        );
    }

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
            msg.setAutorGuest(
                body.getAutorNome() != null ? body.getAutorNome() : "Convidado"
            );
        }

        msg.setConteudo(body.getConteudo());

        return ResponseEntity.ok(toMessageDTO(messageRepo.save(msg)));
    }

    private ChatMessageDTO toMessageDTO(ChatMessage m) {
        return new ChatMessageDTO(
                m.getId(),
                m.getAutor() != null ? m.getAutor().getId() : null,
                m.getAutor() != null ? m.getAutor().getNome() : m.getAutorGuest(),
                m.getConteudo(),
                m.getEnviadoEm(),
                m.isLido()
        );
    }

    @PostMapping("/threads/processo/{processoId}")
    public ResponseEntity<ChatThread> criarOuObterThreadDoProcesso(@PathVariable Long processoId) {

        Processo processo = processoRepository.findById(processoId).orElse(null);
        if (processo == null) return ResponseEntity.badRequest().build();

        ChatThread existente = threadRepo.findFirstByProcessoId(processoId);
        if (existente != null) return ResponseEntity.ok(existente);

        ChatThread novo = new ChatThread();
        novo.setProcesso(processo);
        novo.setTitulo(processo.getCodigo() + " - " + processo.getTitulo());

        ChatThread salvo = threadRepo.save(novo);
        return ResponseEntity.ok(salvo);
    }

}
