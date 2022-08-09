package nfl.telegram.bot;

import nfl.telegram.bot.domian.BotUser;
import nfl.telegram.bot.domian.Team;
import nfl.telegram.bot.service.botService.BotMessageService;
import nfl.telegram.bot.service.botService.BotOperationService;
import nfl.telegram.bot.service.nflApiService.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

import static nfl.telegram.bot.constant.BotCommand.*;

@Component
@PropertySource("classpath:application.properties")
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String BOT_NAME;

    @Value("${bot.token}")
    private String TOKEN;

    @Value("${bot.message.incorrect_value}")
    private String INCORRECT_VALUE_MESSAGE;

    @Value("${bot.message.choose_team}")
    private String CHOOSE_TEAM_MESSAGE;

    @Value("${bot.message.schedule_for_team}")
    private String SCHEDULE_FOR_TEAM;

    private final BotMessageService botMessageService;
    private final BotOperationService botOperationService;
    private final ApiService apiService;

    @Autowired
    public Bot(BotMessageService botService, BotOperationService botOperationService, ApiService apiService) {
        this.botMessageService = botService;
        this.botOperationService = botOperationService;
        this.apiService = apiService;
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
                case BYE_WEEKS:
                    executeMessage(botOperationService.showByeWeeks(update));
                    break;
                case BYE_WEEK_MY_TEAM:
                    executeMessage(botOperationService.showByeWeekForFavoriteTeam(update));
                    break;
                case CURRENT_WEEK:
                    executeMessage(botOperationService.showCurrentWeek(update));
                    break;
                case SCHEDULE:
                    executeMessage(botOperationService.showScheduleForFavoriteTeam(update));
                    break;
                case CURRENT_WEEK_SCHEDULE:
                    executeMessage(botOperationService.showScheduleForCurrentWeek(update));
                    break;
                case SHOW_SCHEDULE_FOR_TEAM:
                    executeMessage(botOperationService.chooseScheduleTeam(update));
                    break;
                default:
                    executeMessage(botMessageService.sendSimpleMessage(update, INCORRECT_VALUE_MESSAGE));
            }
        }

        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getMessage().getText().equals(CHOOSE_TEAM_MESSAGE)) {
                executeEditMessageText(botOperationService.sendMessageOfSelectedTeam(update));
            } else if (update.getCallbackQuery().getMessage().getText().equals(SCHEDULE_FOR_TEAM)) {
                executeEditMessageText(botOperationService.showScheduleForTeam(update));
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

    private void executeEditMessageText(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
