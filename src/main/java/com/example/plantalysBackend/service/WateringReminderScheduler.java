package com.example.plantalysBackend.service;

import com.example.plantalysBackend.model.WateringReminder;
import com.example.plantalysBackend.repository.WateringReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WateringReminderScheduler {

    @Autowired
    private WateringReminderRepository wateringreminderRepo; 
    @Autowired
    private EmailService emailService;

    // Exécuté tous les jours à 8h du matin
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyReminders() {
        LocalDateTime now = LocalDateTime.now();
        
        List<WateringReminder> dueReminders = wateringreminderRepo.findAll().stream()
                .filter(r -> r.getNextReminder().isBefore(now))
                .toList();

        for (WateringReminder reminder : dueReminders) {
            String email = reminder.getUser().getEmail();
            String name = reminder.getUser().getFirstname();
            String plant = reminder.getPlant().getName();

            // Envoi du mail
            emailService.sendWateringReminder(email, plant, name);

            // Replanifie le prochain rappel
            int interval = 7 / reminder.getFrequencyPerWeek();
            reminder.setNextReminder(now.plusDays(interval));

            wateringreminderRepo.save(reminder); 
        }
    }
}
