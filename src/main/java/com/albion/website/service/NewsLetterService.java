package com.albion.website.service;

import com.albion.website.model.Subscriber;
import com.albion.website.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class NewsLetterService {

    private final SubscriberRepository subscriberRepository;
    private final EmailService emailService;

    public void subscribe(String email) {
        if (subscriberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already subscribed");
        }
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(email);
        subscriber.setActive(true);
        subscriberRepository.save(subscriber);

        sendWelcomeEmail(subscriber);
    }

    public void unsubscribe(String email) {
        Subscriber subscriber = subscriberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Subscriber not found"));
        subscriber.setActive(false);
        subscriberRepository.save(subscriber);
    }

    private void sendWelcomeEmail(Subscriber subscriber) {
        emailService.sendEmail(
                subscriber.getEmail(),
                "Thanks for subscribing!",
                "Thank you for subscribing to our newsletter! We're excited to have you with us."
        );
    }

    @Scheduled(cron = "0 0 9 1 * *")
    public void sendAnniversaryEmails() {
        List<Subscriber> activeSubscribers = subscriberRepository.findAllByActiveTrue();
        LocalDateTime now = LocalDateTime.now();

        for (Subscriber subscriber : activeSubscribers) {
            long months = ChronoUnit.MONTHS.between(subscriber.getCreatedAt(), now);

            if (months <= 0) {
                continue;
            }

            String message = buildAnniversaryMessage(months);
            emailService.sendEmail(
                    subscriber.getEmail(),
                    "Happy Anniversary with Us!",
                    message
            );
        }
    }

    private String buildAnniversaryMessage(long months) {
        if (months % 12 == 0) {
            long years = months / 12;
            String yearWord = years == 1 ? "year" : "years";
            return String.format("Wow, you've been with us for %d %s! Thank you for staying with us.", years, yearWord);
        } else {
            String monthWord = months == 1 ? "month" : "months";
            return String.format("You've been with us for %d %s! Thanks for sticking around.", months, monthWord);
        }
    }

}