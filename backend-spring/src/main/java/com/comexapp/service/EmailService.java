package com.comexapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    public void enviarEmail(String para, String assunto, String mensagem) throws Exception {

        URL url = new URL("https://api.brevo.com/v3/smtp/email");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("accept", "application/json");
        con.setRequestProperty("api-key", apiKey);
        con.setRequestProperty("content-type", "application/json");
        con.setDoOutput(true);
  
        String html = "<html><body>" + mensagem.replace("\n", "<br>") + "</body></html>";


        String json = """

        {
          "sender": { "name": "%s", "email": "%s" },
          "to": [{ "email": "%s" }],
           "subject": "%s",
           "htmlContent": "%s"
        }
        """.formatted(senderName, senderEmail, para, assunto, html);
  

        try (OutputStream os = con.getOutputStream()) {
            os.write(json.getBytes());
        }

        int status = con.getResponseCode();

        System.out.println("BREVO STATUS = " + status); // <-- LOG para depuração

        if (con.getErrorStream() != null) {
    String err = new String(con.getErrorStream().readAllBytes());
    System.out.println("BREVO ERROR BODY = " + err); // <-- LOG para depuração 
}

        if (status != 201 && status != 200) {
    throw new RuntimeException("Erro ao enviar e-mail (HTTP " + status + ")");
}
    }
}
