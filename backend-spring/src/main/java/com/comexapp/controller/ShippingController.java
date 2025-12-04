package com.comexapp.controller;

/*
A classe ShippingController.java é um controlador REST responsável por gerenciar 
operações relacionadas a instruções de embarque (shipping instructions). 
Ela recebe requisições HTTP contendo dados de shipping, gera um documento Word 
(.docx) através do serviço ShippingWordService e retorna o arquivo para download.
*/

import com.comexapp.DTO.ShippingInstructionDTO;
import com.comexapp.service.ShippingWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// Definição do controlador REST que fornece endpoint para gerar arquivos Word
@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    @Autowired
    private ShippingWordService wordService;

    // Endpoint que recebe dados de shipping e retorna documento Word
    @PostMapping("/docx")
    public ResponseEntity<byte[]> gerarWord(@RequestBody ShippingInstructionDTO dto) {
        // Gera o arquivo .docx usando o serviço
        byte[] docx = wordService.gerarDocx(dto);

        // Se ocorrer algum problema, retorna erro 500
        if (docx == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Configura headers HTTP para download do arquivo
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        headers.setContentDispositionFormData("filename", "shipping_instruction.docx");

        // Retorna o arquivo com status 200
        return new ResponseEntity<>(docx, headers, HttpStatus.OK);
    }
}
