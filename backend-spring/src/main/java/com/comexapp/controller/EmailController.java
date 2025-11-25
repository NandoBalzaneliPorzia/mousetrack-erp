package com.comexapp.controller;

import com.comexapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import com.comexapp.DTO.EmailRequestDTO;
import org.springframework.web.bind.annotation.*;

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
