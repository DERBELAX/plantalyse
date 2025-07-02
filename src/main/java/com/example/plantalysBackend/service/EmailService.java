package com.example.plantalysBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWateringReminder(String to, String plantName, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ad.plantalys@gmail.com"); 
        message.setTo(to);
        message.setSubject("Rappel d’arrosage - Plantalys");
        message.setText("Bonjour " + userName + ",\n\nC’est le moment d’arroser votre plante : " + plantName + " 🌱\n\nÀ bientôt,\nL’équipe Plantalys");

        mailSender.send(message);
    }
}

