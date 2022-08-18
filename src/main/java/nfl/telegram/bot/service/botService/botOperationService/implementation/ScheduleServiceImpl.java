package nfl.telegram.bot.service.botService.botOperationService.implementation;

import nfl.telegram.bot.domian.Schedule;
import nfl.telegram.bot.domian.Team;
import nfl.telegram.bot.service.botService.botButtonService.BotButtonService;
import nfl.telegram.bot.service.botService.botMessageService.BotMessageService;
import nfl.telegram.bot.service.botService.botOperationService.ScheduleService;
import nfl.telegram.bot.service.dataBaseService.dataServcie.DataService;
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
public class ScheduleServiceImpl implements ScheduleService {

    @Value("${bot.message.schedule_for_any_team_message}")
    private String SCHEDULE_FOR_ANY_TEAM_MESSAGE;
    @Value("${bot.message.select_team_message}")
    private String SELECT_TEAM_MESSAGE;

    private final BotMessageService botMessageService;
    private final ApiService apiService;
    private final DataService dataService;
    private final BotButtonService botButtonService;

    private final SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("E, d MMM HH:mm", Locale.ENGLISH);

    @Autowired
    public ScheduleServiceImpl(BotMessageService botMessageService,
                               ApiService apiService,
                               DataService dataService,
                               BotButtonService botButtonService) {
        this.botMessageService = botMessageService;
        this.apiService = apiService;
        this.dataService = dataService;
        this.botButtonService = botButtonService;
    }

    @Override
    public SendMessage showCurrentWeek(Update update) {
        return botMessageService.sendSimpleMessage(update, apiService.getCurrentWeek());
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
                        .append(simpleDateFormat.format(schedule.getDate()))
                        .append("\n\n"));
        return botMessageService.sendSimpleMessage(update, stringBuilder.toString());
    }

    @Override
    public SendMessage showScheduleForFavoriteTeam(Update update) {
        Team team = dataService.getBotUser(update.getMessage().getFrom().getId()).getTeam();
        if (team == null) {
            return botMessageService.sendSimpleMessage(update, SELECT_TEAM_MESSAGE);
        } else {
            List<Schedule> scheduleList = apiService.getSeasonSchedule();
            String message = getScheduleAsStringMessage(scheduleList, team);
            return botMessageService.sendSimpleMessage(update, message);
        }
    }

    @Override
    public SendMessage chooseAnyTeamForSchedule(Update update) {
        SendMessage sendMessage = botMessageService.sendSimpleMessage(update, SCHEDULE_FOR_ANY_TEAM_MESSAGE);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
    }

    @Override
    public EditMessageText showScheduleForAnyTeam(Update update) {
        Team team = Team.valueOf(update.getCallbackQuery().getData());
        List<Schedule> scheduleList = apiService.getSeasonSchedule();
        String message = getScheduleAsStringMessage(scheduleList, team);
        return botMessageService.sendEditMessageText(update, message);
    }

    private String getScheduleAsStringMessage(List<Schedule> scheduleList, Team team) {
        StringBuilder stringBuilder = new StringBuilder();
        scheduleList.stream()
                .filter(schedule -> schedule.getDate() != null)
                .filter(schedule ->
                        schedule.getHomeTeam().equals(String.valueOf(team)) || schedule.getAwayTeam().equals(String.valueOf(team)))
                .forEach(schedule -> stringBuilder
                        .append(schedule.getAwayTeam())
                        .append("@")
                        .append(schedule.getHomeTeam())
                        .append(" on ")
                        .append(simpleDateFormat.format(schedule.getDate()))
                        .append("\n\n"));
        return stringBuilder.toString();
    }


}
