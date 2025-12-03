package com.comexapp.controller;

/*
A classe ShippingController.java é um controlador REST responsável por gerenciar 
operações relacionadas a instruções de embarque (shipping instructions). Ele expõe 
um endpoint para gerar documentos Word (.docx) a partir de dados de instruções de embarque.
*/

import com.comexapp.DTO.ShippingInstructionDTO;
import com.comexapp.service.ShippingWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

//definição endpoint REST geerando e devolvendo um arquivo word para download
@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    @Autowired
    private ShippingWordService wordService;

    @PostMapping("/docx")
    public ResponseEntity<byte[]> gerarWord(@RequestBody ShippingInstructionDTO dto) {
        byte[] docx = wordService.gerarDocx(dto);

        if (docx == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        headers.setContentDispositionFormData("filename", "shipping_instruction.docx");

        return new ResponseEntity<>(docx, headers, HttpStatus.OK);
    }
}
