package com.Bleedy;

import com.Bleedy.repos.UserRepo;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;


@SpringBootApplication
@EntityScan
@EnableJpaRepositories
public class Application {
    @Bean
    YouTube youTube() throws GeneralSecurityException, IOException {
        return YoutubeApi.getService();
    }


    public static void main(String[] args) throws GeneralSecurityException, IOException {
        ApiContextInitializer.init();

        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        UserRepo repository = context.getBean(UserRepo.class);

        Iterable<User> users = repository.findAll();
        System.out.println("Users found with findAll():");
        System.out.println("-------------------------------");
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println();


        MyTelegramBot telegramBot = (MyTelegramBot) context.getBean("myTelegramBot");
        TelegramBotsApi botsApi  = new TelegramBotsApi();

        try {
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}