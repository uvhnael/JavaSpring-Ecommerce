package org.uvhnael.ecomapi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uvhnael.ecomapi.Model.Mail;
import org.uvhnael.ecomapi.Service.MailService;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping
    public ResponseEntity<?> sendMail(@RequestBody Mail mail) {

        return ResponseEntity.status(HttpStatus.OK).body("Mail sent successfully");
    }
}
