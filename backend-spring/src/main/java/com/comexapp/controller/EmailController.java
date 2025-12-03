package com.comexapp.controller;

/*
A classe EmailController.java é um controlador REST responsável por 
gerenciar as operações relacionadas ao envio de e-mails. Ele expõe 
um endpoint para receber requisições de envio de e-mail e utiliza o 
EmailService para processá-las.
*/

import com.comexapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import com.comexapp.DTO.EmailRequestDTO;
import org.springframework.web.bind.annotation.*;

//definição do controlador REST - recebe http, envia para EmailService e retorna confirmação OK
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviar")
public String enviar(@RequestBody EmailRequestDTO req) throws Exception {

    emailService.enviarEmail(
        req.getPara(),
        req.getAssunto(),
        req.getMensagem()
    );

    return "OK";
}
}
