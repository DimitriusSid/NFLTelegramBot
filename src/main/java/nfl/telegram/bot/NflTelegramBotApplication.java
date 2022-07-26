package nfl.telegram.bot;

import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class NflTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(NflTelegramBotApplication.class, args);

    }

    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }

}
