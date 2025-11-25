package com.comexapp.controller;

import com.comexapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import com.comexapp.DTO.EmailRequestDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviar")
public String enviar(@RequestBody EmailRequestDTO req) {

    String assunto = "Link do processo " + req.getProcessoId();
    String texto = "Segue o link do processo:\n\n" + req.getLink();

    emailService.enviarEmail(req.getEmail(), assunto, texto);

    return "OK";
}
}
