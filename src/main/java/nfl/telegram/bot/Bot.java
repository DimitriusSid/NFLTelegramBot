package nfl.telegram.bot;

import nfl.telegram.bot.service.botService.BotMessageService;
import nfl.telegram.bot.service.botService.BotOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static nfl.telegram.bot.constant.BotCommand.*;

@Component
@PropertySource("classpath:application.properties")
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String BOT_NAME;

    @Value("${bot.token}")
    private String TOKEN;

    private final BotMessageService botMessageService;
    private final BotOperationService botOperationService;

    @Autowired
    public Bot(BotMessageService botService, BotOperationService botOperationService) {
        this.botMessageService = botService;
        this.botOperationService = botOperationService;
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
                    executeMessage(botOperationService.sendGreetingsMessage(update));
                    break;
                case CHOOSE_FAVORITE_TEAM:
                case CHANGE_FAVORITE_TEAM:
                    executeMessage(botOperationService.chooseFavoriteTeam(update));
                    break;
                default:
                    executeMessage(botMessageService.sendSimpleMessage(update, "Incorrect value. Please, try again"));
            }
        }


        if (update.hasCallbackQuery()) {
            try {
                execute(botOperationService.sendMessageOfSelectedTeam(update));
                try {
                    Thread.sleep(5000);
                    execute(botMessageService.createEditMessageText(update, "You are loch after 1"));
                    System.out.println("done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
