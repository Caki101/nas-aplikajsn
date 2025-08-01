package com.backend.Backend.services;

import com.backend.Backend.security.SecurityData;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MailgunService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void sendEmail(String toEmail, String subject, String content, String type_of_email) {
        // temp fix
        if (!toEmail.contains("@")) return;

        String url = "https://api.eu.mailgun.net/v3/" + SecurityData.MAILGUN_DOMAIN + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("api", SecurityData.MAILGUN_API);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "from="
                + type_of_email
                + "@"
                + SecurityData.MAILGUN_DOMAIN
                + "&to=" + toEmail
                + "&subject=" + subject
                + "&html=" + content;

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        System.err.println("Mailgun Response: " + response.getBody());
    }
}
