package com.example.biblioteca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.biblioteca.DTO.EmailDTO;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailDTO email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("kaubiblioteca@gmail.com");
            message.setTo(email.to());
            message.setSubject(email.subject());
            message.setText(email.body());
    
            mailSender.send(message);
            System.out.println("E-mail enviado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
