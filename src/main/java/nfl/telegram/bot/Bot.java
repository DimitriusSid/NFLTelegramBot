package nfl.telegram.bot;

import nfl.telegram.bot.service.botService.BotMessageService;
import nfl.telegram.bot.service.botService.BotOperationService;
import nfl.telegram.bot.service.nflApiService.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
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

    @Value("${bot.message.incorrect_value}")
    private String INCORRECT_VALUE_MESSAGE;

    @Value("${bot.message.choose_team}")
    private String CHOOSE_TEAM_MESSAGE;

    @Value("${bot.message.schedule_for_team}")
    private String TEAM_SCHEDULE;

    @Value("${bot.message.byeweek_for_team}")
    private String BYEWEEK_FOR_TEAM;

    @Value("${bot.message.standing_for_team}")
    private String STANDING_FOR_TEAM;

    @Value("${bot.message.nfl_team_info}")
    private String NFL_TEAM_INFO;


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
                case BYE_WEEK_FOR_ANY_TEAM:
                    executeMessage(botOperationService.chooseByeWeekTeam(update));
                    break;
                case CURRENT_WEEK:
                    executeMessage(botOperationService.showCurrentWeek(update));
                    break;
                case SCHEDULE_FOR_FAVORITE_TEAM:
                    executeMessage(botOperationService.showScheduleForFavoriteTeam(update));
                    break;
                case CURRENT_WEEK_SCHEDULE:
                    executeMessage(botOperationService.showScheduleForCurrentWeek(update));
                    break;
                case SCHEDULE_FOR_TEAM:
                    executeMessage(botOperationService.chooseScheduleTeam(update));
                    break;
                case STANDING_FOR_FAVORITE_TEAM:
                    executeMessage(botOperationService.showStandingForFavoriteTeam(update));
                    break;
                case STANDING_FOR_ANY_TEAM:
                    executeMessage(botOperationService.chooseStandingTeam(update));
                    break;
                case FAVORITE_NFL_TEAM_INFO:
                    executeMessage(botOperationService.showFavoriteNFLTeamInfo(update));
                    break;
                case ANY_NFL_TEAM_INFO:
                    executeMessage(botOperationService.chooseNFLTeam(update));
                    break;
                default:
                    executeMessage(botMessageService.sendSimpleMessage(update, INCORRECT_VALUE_MESSAGE));
            }
        }

        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getMessage().getText().equals(CHOOSE_TEAM_MESSAGE)) {
                executeEditMessageText(botOperationService.sendMessageOfSelectedTeam(update));
            } else if (update.getCallbackQuery().getMessage().getText().equals(TEAM_SCHEDULE)) {
                executeEditMessageText(botOperationService.showScheduleForTeam(update));
            } else if (update.getCallbackQuery().getMessage().getText().equals(BYEWEEK_FOR_TEAM)) {
                executeEditMessageText(botOperationService.showByeWeekForAnyTeam(update));
            } else if (update.getCallbackQuery().getMessage().getText().equals(STANDING_FOR_TEAM)) {
                executeEditMessageText(botOperationService.showStandingForTeam(update));
            } else if (update.getCallbackQuery().getMessage().getText().equals(NFL_TEAM_INFO)) {
                executeEditMessageText(botOperationService.showAnyNFLTeamInfo(update));
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
