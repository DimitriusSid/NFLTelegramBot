package nfl.telegram.bot;

import nfl.telegram.bot.service.botService.BotMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static nfl.telegram.bot.constant.BotCommand.CHOOSE_FAVORITE_TEAM;
import static nfl.telegram.bot.constant.BotCommand.START;

@Component
@PropertySource("classpath:application.properties")
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String BOT_NAME;

    @Value("${bot.token}")
    private String TOKEN;

    private final BotMessageService botService;

    @Autowired
    public Bot(BotMessageService botService) {
        this.botService = botService;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String incomingTextMessage = update.getMessage().getText();
            switch (incomingTextMessage) {
                case START:
                    executeMessage(botService.sendGreetingMessage(update));
                    break;
                case CHOOSE_FAVORITE_TEAM:
                    executeMessage(botService.chooseFavoriteTeam(update));
                    break;
                default:
                    executeMessage(botService.sendSimpleMessage(update, "Incorrect value. Please, try again"));
            }
        }


        if (update.hasCallbackQuery()) {
            try {
                execute(botService.createEditMessageText(update, null));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
