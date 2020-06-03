package com.Bleedy;

import com.google.api.services.youtube.YouTube;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Entity;
import java.io.IOException;
import java.security.GeneralSecurityException;

//@Configuration
//@ComponentScan(basePackages = "com.Bleedy")
//@EntityScan
public class BotConfig {

    //@Bean
    YouTube youTube() throws GeneralSecurityException, IOException {
        System.out.println("init youtube bean");
        return YoutubeApi.getService();
    }

}
