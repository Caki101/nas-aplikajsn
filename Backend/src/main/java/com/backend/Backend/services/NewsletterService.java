package com.backend.Backend.services;

import com.backend.Backend.dataTypes.NewsletterSubscriber;
import com.backend.Backend.dataTypes.Tiket;
import com.backend.Backend.repositories.NewsletterRepo;
import com.backend.Backend.repositories.TiketiRepo;
import com.backend.Backend.security.SecurityData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsletterService {
    private final MailgunService mailgunService;
    private final TiketiRepo tiketiRepo;
    private final NewsletterRepo newsletterRepo;

    @Autowired
    public NewsletterService(MailgunService mailgunService,
                             TiketiRepo tiketiRepo,
                             NewsletterRepo newsletterRepo) {
        this.mailgunService = mailgunService;
        this.tiketiRepo = tiketiRepo;
        this.newsletterRepo = newsletterRepo;
    }

    // needs testing
    @Scheduled(cron = "0 0 12 1 * ?") // every first of month at 12:00:00
    public void sendNews() {
        Tiket tiket = tiketiRepo.findAveragePriceTiket();
        List<NewsletterSubscriber> nss = new ArrayList<>();

        newsletterRepo.findAll().forEach(ns ->
            mailgunService.sendEmail(
                ns.getEmail(),
                "Monthly Newsletter Best Offer",
                "<div><a href='http://" + SecurityData.FRONTEND_ORIGIN + "/tiket/" + tiket.getId() + "'></a></div>",
                    "newsletter"
            )
        );
    }
}
