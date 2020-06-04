package com.Bleedy;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.Bleedy.repos.UserRepo;


import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    YouTube youtubeService;

    Integer count = 0;
    Boolean countForRegister = false;
    Boolean countForItem = false;

    public MyTelegramBot(){}




    @Override
    public void onUpdateReceived(Update update) {
        //top list with bad tags
        if (update.hasMessage() && update.getMessage().getText().contains("/toplistwitblock")) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                YouTube.Videos.List request = youtubeService.videos()
                        .list("snippet,contentDetails,statistics");
                VideoListResponse response = request.setChart("mostPopular")
                        .setRegionCode("RU")
                        .setMaxResults(15L)
                        .execute();
                Iterable<User> users = userRepo.findAll();
                for (User user : users) {
                    if (user.getUserIdentification().equals(update.getMessage().getChat().getId())) {
                        for (Video item : response.getItems()) {
                            for (String tag : user.getBlackListTags()) {
                                if (item.getSnippet().getTags() == null || item.getSnippet().getTags().contains(tag)) {
                                    countForItem = true;
                                }
                            }
                            for (String channel : user.getBlackListChanel()) {
                                if (item.getSnippet().getChannelTitle().contains(channel)) {
                                    countForItem = true;
                                }
                            }
                            if (countForItem) {
                                //System.out.println("skip");
                            } else {
                                if (count < 10) {
                                    stringBuilder.append("www.youtube.com/watch?v=" + item.getId() +
                                            "\n" + item.getSnippet().getTitle() + "\\ " + item.getSnippet().getChannelTitle() + ";" + "\n");
                                    //System.out.println("www.youtube.com/watch?v=" + item.getId() +
                                    //        "\n" + item.getSnippet().getTitle() + "\\"+ item.getSnippet().getChannelTitle() + "\n"+ item.getSnippet().getTags());
                                    count++;
                                }
                            }
                            countForItem = false;
                        }
                    }
                }

                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText(stringBuilder.toString());
                count =0;
                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // register
        if (update.getMessage().getText().contentEquals("/register")) {
            Iterable<User> users = userRepo.findAll();
            for (User user : users) {
                if (user.getUserIdentification().equals(update.getMessage().getChat().getId())) {
                    countForRegister = true;
                }
            }
            if (countForRegister) {
                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText("You are registered");
                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                userRepo.save(new User(update.getMessage().getChatId()));
                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText("Done :)" + "Now you can add tag and chanel in blacklist");

                countForRegister = false;
                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        // add tag
        if (update.getMessage().getText().contains("/addtag")) {
            Iterable<User> users = userRepo.findAll();
            for (User user : users) {
                if (user.getUserIdentification().equals(update.getMessage().getChat().getId())) {
                    user.addTagInBlackList(update.getMessage().getText().replace("/addtag ", "").toLowerCase());
                    userRepo.save(user);

                    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setText("Done :)");
                    try {
                        execute(message); // Call method to send the message
                    } catch (TelegramApiException e) {
                        e.printStackTrace();

                    }
                }
            }
        }
        // add tag
        if (update.getMessage().getText().contains("/addchannel")) {
            Iterable<User> users = userRepo.findAll();
            for (User user : users) {
                if (user.getUserIdentification().equals(update.getMessage().getChat().getId())) {
                    user.addChanelInBlackList(update.getMessage().getText().replace("/addchannel ", ""));
                    userRepo.save(user);
                    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setText("Done :)");
                    try {
                        execute(message); // Call method to send the message
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (update.hasMessage() && update.getMessage().isCommand() && update.getMessage().getText().contentEquals("/help")) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText("/addtag - add tag in blacklist (one at a time)\n" +
                            "/addchannel - add channel in blacklist (one at a time)");
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }

    }



    @Override
    public String getBotUsername() {
        return "First23_Bot";
    }
    @Override
    public String getBotToken() {
        return "1187356229:AAEW5BtrdvO8IqtG0jY_oD5sq3KdjZQ4kqU";
    }
}