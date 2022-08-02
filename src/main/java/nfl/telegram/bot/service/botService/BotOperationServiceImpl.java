package nfl.telegram.bot.service.botService;

import nfl.telegram.bot.domian.BotUser;
import nfl.telegram.bot.domian.ByeWeek;
import nfl.telegram.bot.domian.Team;
import nfl.telegram.bot.service.dataBaseService.DataService;
import nfl.telegram.bot.service.nflApiService.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Predicate;

@Service
public class BotOperationServiceImpl implements BotOperationService {

    @Value("${bot.message.choose_team}")
    private String CHOOSE_TEAM_MESSAGE;
    @Value("${bot.message.greeting}")
    private String GREETING_MESSAGE;
    @Value("${bot.message.selected_team}")
    private String SELECTED_TEAM;

    private final BotMessageService botMessageService;
    private final BotButtonService botButtonService;
    private final DataService dataService;
    private final ApiService apiService;

    @Autowired
    public BotOperationServiceImpl(BotMessageService botMessageService, BotButtonService botButtonService, DataService dataService, ApiService apiService) {
        this.botMessageService = botMessageService;
        this.botButtonService = botButtonService;
        this.dataService = dataService;
        this.apiService = apiService;
    }

    @Override
    public SendMessage chooseFavoriteTeam(Update update) {
        SendMessage sendMessage = botMessageService.sendSimpleMessage(update, CHOOSE_TEAM_MESSAGE);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
    }

    @Override
    public SendMessage sendGreetingsMessage(Update update) {
        return botMessageService.sendSimpleMessage(update, String.format(GREETING_MESSAGE,
                update.getMessage().getChat().getFirstName()));
    }

    @Override
    public EditMessageText sendMessageOfSelectedTeam(Update update) {
        saveBotUser(createBotUser(update));
        return botMessageService.createEditMessageText(update, SELECTED_TEAM);
    }

    @Override
    public SendMessage showByeWeeks(Update update) {
        List<ByeWeek> byeWeeks = apiService.getByeWeeks();
        StringBuilder stringBuilder = new StringBuilder();
        byeWeeks.forEach(byeWeek -> stringBuilder
                .append("Team ")
                .append(byeWeek.getTeam())
                .append(" on week ")
                .append(byeWeek.getWeek())
                .append("\n"));
        return botMessageService.sendSimpleMessage(update, stringBuilder.toString());

    }

    @Override
    public SendMessage showByeWeekForFavoriteTeam(Update update) {
        List<ByeWeek> byeWeeks = apiService.getByeWeeks();
        StringBuilder stringBuilder = new StringBuilder();
        Team team = dataService.getBotUser(update.getMessage().getFrom().getId()).getTeam();
        byeWeeks.stream()
                .filter(byeWeek -> byeWeek.getTeam().equals(team))
                .forEach(byeWeek -> stringBuilder
                        .append("Team ")
                        .append(byeWeek.getTeam())
                        .append(" on week ")
                        .append(byeWeek.getWeek()));
        return botMessageService.sendSimpleMessage(update, stringBuilder.toString());
    }

    @Override
    public SendMessage showCurrentWeek(Update update) {
        return botMessageService.sendSimpleMessage(update, apiService.getCurrentWeek());
    }

    @Override
    public BotUser createBotUser(Update update) {
        Long userId = update.getCallbackQuery().getFrom().getId();
        Team team = Team.valueOf(update.getCallbackQuery().getData());
        return new BotUser(userId, team);
    }

    @Override
    public BotUser saveBotUser(BotUser botUser) {
        return dataService.saveBotUser(botUser);
    }


}
