package com.comexapp.controller;

/*
Responsável: Ana Beatriz Maranho
A classe EmailController.java é um controlador REST que gerencia o envio 
de e-mails na aplicação. Ele recebe requisições HTTP, repassa os dados 
para o EmailService e retorna uma confirmação de sucesso.
*/

import com.comexapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import com.comexapp.DTO.EmailRequestDTO;
import org.springframework.web.bind.annotation.*;

// Controlador REST responsável por endpoints de e-mail
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Endpoint POST para envio de e-mail
    @PostMapping("/enviar")
    public String enviar(@RequestBody EmailRequestDTO req) throws Exception {

        // Chama o serviço de envio de e-mail com os dados recebidos
        emailService.enviarEmail(
            req.getPara(),
            req.getAssunto(),
            req.getMensagem()
        );

        // Retorna confirmação de envio
        return "OK";
    }
}
