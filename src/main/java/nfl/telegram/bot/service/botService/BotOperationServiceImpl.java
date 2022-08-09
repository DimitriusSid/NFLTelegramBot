package nfl.telegram.bot.service.botService;

import nfl.telegram.bot.domian.BotUser;
import nfl.telegram.bot.domian.ByeWeek;
import nfl.telegram.bot.domian.Schedule;
import nfl.telegram.bot.domian.Team;
import nfl.telegram.bot.service.dataBaseService.DataService;
import nfl.telegram.bot.service.nflApiService.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Service
public class BotOperationServiceImpl implements BotOperationService {

    @Value("${bot.message.choose_team}")
    private String CHOOSE_TEAM_MESSAGE;
    @Value("${bot.message.greeting}")
    private String GREETING_MESSAGE;
    @Value("${bot.message.selected_team}")
    private String SELECTED_TEAM;
    @Value("${bot.message.schedule_for_team}")
    private String SCHEDULE_FOR_TEAM;


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
        saveBotUser(update);
        return botMessageService.sendSimpleMessage(update, String.format(GREETING_MESSAGE,
                update.getMessage().getChat().getFirstName()));
    }

    @Override
    public EditMessageText sendMessageOfSelectedTeam(Update update) {
        saveBotUser(update);
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
        ByeWeek byeWeek = apiService.getByeWeekForTeam(update);
        if (byeWeek == null) {
            return botMessageService.sendSimpleMessage(update, "Tap /chooseTeam and select yor favorite team");
        } else {
            String message = "Team " + byeWeek.getTeam() + " on week " + byeWeek.getWeek();
            return botMessageService.sendSimpleMessage(update, message);
        }
    }

    @Override
    public SendMessage showCurrentWeek(Update update) {
        return botMessageService.sendSimpleMessage(update, apiService.getCurrentWeek());
    }

    @Override
    public SendMessage showScheduleForFavoriteTeam(Update update) {
        List<Schedule> scheduleList = apiService.getSeasonSchedule();
        StringBuilder stringBuilder = new StringBuilder();
        scheduleList.stream()
                .filter(schedule -> schedule.getHomeTeam()
                        .equals(String.valueOf(dataService.getBotUser(update.getMessage().getFrom().getId()).getTeam()))
                        || schedule.getAwayTeam()
                        .equals(String.valueOf(dataService.getBotUser(update.getMessage().getFrom().getId()).getTeam())))
                .forEach(schedule -> stringBuilder
                        .append(schedule.getAwayTeam())
                        .append("@")
                        .append(schedule.getHomeTeam())
                        .append(" on ")
                        .append(schedule.getDate())
                        .append("\n"));
        return botMessageService.sendSimpleMessage(update, stringBuilder.toString());
    }

    @Override
    public SendMessage showScheduleForCurrentWeek(Update update) {
        List<Schedule> scheduleList = apiService.getSeasonSchedule();
        StringBuilder stringBuilder = new StringBuilder();


        scheduleList.stream()
                .filter(schedule -> schedule.getWeek() == Integer.parseInt(apiService.getCurrentWeek()))
                .forEach(schedule -> stringBuilder
                        .append(schedule.getAwayTeam())
                        .append("@")
                        .append(schedule.getHomeTeam())
                        .append(" on ")
                        .append(schedule.getDate())
                        .append("\n\n"));
        return botMessageService.sendSimpleMessage(update, stringBuilder.toString());
    }

    @Override
    public SendMessage chooseScheduleTeam(Update update) {
        SendMessage sendMessage = botMessageService.sendSimpleMessage(update, SCHEDULE_FOR_TEAM);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
    }

    @Override
    public EditMessageText showScheduleForTeam(Update update) {
        String team = update.getCallbackQuery().getData();
        List<Schedule> scheduleList = apiService.getSeasonSchedule();
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, d MMM HH:mm", Locale.ENGLISH);
        scheduleList.stream()
                .filter(schedule -> schedule.getDate() != null)
                .filter(schedule ->
                        schedule.getHomeTeam().equals(team) || schedule.getAwayTeam().equals(team))
                .forEach(schedule -> stringBuilder
                        .append(schedule.getAwayTeam())
                        .append("@")
                        .append(schedule.getHomeTeam())
                        .append(" on ")
                        .append(simpleDateFormat.format(schedule.getDate()))
                        .append("\n\n"));
        System.out.println(stringBuilder.length());
        return botMessageService.createEditMessageText(update, stringBuilder.toString());
    }

    @Override
    public BotUser saveBotUser(Update update) {
        if (update.getMessage() != null) {
            Long userId = update.getMessage().getFrom().getId();
            return dataService.saveBotUser(new BotUser(userId, null));
        } else {
            Long userId = update.getCallbackQuery().getFrom().getId();
            Team team = Team.valueOf(update.getCallbackQuery().getData());
            return dataService.saveBotUser(new BotUser(userId, team));
        }
    }

}
