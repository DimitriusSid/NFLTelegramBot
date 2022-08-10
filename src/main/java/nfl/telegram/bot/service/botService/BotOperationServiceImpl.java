package nfl.telegram.bot.service.botService;

import nfl.telegram.bot.domian.*;
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
import java.util.Optional;

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
    @Value("${bot.message.byeweek_for_team}")
    private String BYEWEEK_FOR_TEAM;
    @Value("${bot.message.standing_for_team}")
    private String STANDING_FOR_TEAM;
    @Value("${bot.message.nfl_team_info}")
    private String NFL_TEAM_INFO;


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
        List<ByeWeek> byeWeeks = apiService.getByeWeeks();
        BotUser botUser = dataService.getBotUser(update.getMessage().getFrom().getId());
        Team team = botUser.getTeam();
        Optional<ByeWeek> optionalByeWeek = byeWeeks.stream()
                .filter(byeWeek -> byeWeek.getTeam().equals(team)).findAny();
        ByeWeek byeWeek = optionalByeWeek.orElse(null);
        if (byeWeek == null) {
            return botMessageService.sendSimpleMessage(update, "Tap /chooseTeam and select yor favorite team");
        } else {
            String message = "Team " + byeWeek.getTeam() + " on week " + byeWeek.getWeek();
            return botMessageService.sendSimpleMessage(update, message);
        }
    }

    @Override
    public EditMessageText showByeWeekForAnyTeam(Update update) {
        Team team = Team.valueOf(update.getCallbackQuery().getData());
        List<ByeWeek> byeWeeks = apiService.getByeWeeks();
        ByeWeek byeWeek = byeWeeks.stream().filter(b -> b.getTeam().equals(team)).findAny().get();
        String message = "Team " + byeWeek.getTeam() + " on week " + byeWeek.getWeek();
        return botMessageService.createEditMessageText(update, message);
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
    public SendMessage chooseByeWeekTeam(Update update) {
        SendMessage sendMessage = botMessageService.sendSimpleMessage(update, BYEWEEK_FOR_TEAM);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
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

    @Override
    public SendMessage showStandingForFavoriteTeam(Update update) {
        List<Standing> standings = apiService.getSeasonStanding();
        BotUser botUser = dataService.getBotUser(update.getMessage().getFrom().getId());
        Team team = botUser.getTeam();
        Standing standing = standings.stream().filter(s -> s.getTeam().equals(team)).findAny().get();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Team: ").append(standing.getTeam()).append("\n")
                .append("Conference: ").append(standing.getConference()).append("\n")
                .append("Division: ").append(standing.getDivision()).append("\n")
                .append("Wins: ").append(standing.getWins()).append("\n")
                .append("Losses: ").append(standing.getLosses()).append("\n")
                .append("Ties: ").append(standing.getTies()).append("\n")
                .append("Touchdowns: ").append(standing.getTouchdowns()).append("\n")
                .append("DivisionRank: ").append(standing.getDivisionRank()).append("\n")
                .append("ConferenceRank: ").append(standing.getConferenceRank()).append("\n");
        return botMessageService.sendSimpleMessage(update, stringBuilder.toString());
    }

    @Override
    public SendMessage chooseStandingTeam(Update update) {
        SendMessage sendMessage = botMessageService.sendSimpleMessage(update, STANDING_FOR_TEAM);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
    }

    @Override
    public EditMessageText showStandingForTeam(Update update) {
        Team team = Team.valueOf(update.getCallbackQuery().getData());
        List<Standing> standings = apiService.getSeasonStanding();
        Standing standing = standings.stream().filter(s -> s.getTeam().equals(team)).findAny().get();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Team: ").append(standing.getTeam()).append("\n")
                .append("Conference: ").append(standing.getConference()).append("\n")
                .append("Division: ").append(standing.getDivision()).append("\n")
                .append("Wins: ").append(standing.getWins()).append("\n")
                .append("Losses: ").append(standing.getLosses()).append("\n")
                .append("Ties: ").append(standing.getTies()).append("\n")
                .append("Touchdowns: ").append(standing.getTouchdowns()).append("\n")
                .append("DivisionRank: ").append(standing.getDivisionRank()).append("\n")
                .append("ConferenceRank: ").append(standing.getConferenceRank()).append("\n");
        return botMessageService.createEditMessageText(update, stringBuilder.toString());
    }

    @Override
    public SendMessage showFavoriteNFLTeamInfo(Update update) {
        BotUser botUser = dataService.getBotUser(update.getMessage().getFrom().getId());
        Team team = botUser.getTeam();
        List<NFLTeam> nflTeams = apiService.getNFLTeamInfo();
        NFLTeam nflTeam = nflTeams.stream().filter(el -> el.getKey().equals(team)).findAny().get();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("City: ").append(nflTeam.getCity()).append("\n")
                .append("Name: ").append(nflTeam.getName()).append("\n")
                .append("Conference: ").append(nflTeam.getConference()).append("\n")
                .append("Division: ").append(nflTeam.getDivision()).append("\n")
                .append("Head Coach: ").append(nflTeam.getHeadCoach()).append("\n")
                .append("Offensive Coordinator: ").append(nflTeam.getOffensiveCoordinator()).append("\n")
                .append("Defensive Coordinator: ").append(nflTeam.getDefensiveCoordinator()).append("\n")
                .append("Special Teams Coach: ").append(nflTeam.getSpecialTeamsCoach()).append("\n");
        return botMessageService.sendSimpleMessage(update, stringBuilder.toString());
    }

    @Override
    public SendMessage chooseNFLTeam(Update update) {
        SendMessage sendMessage = botMessageService.sendSimpleMessage(update, NFL_TEAM_INFO);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
    }

    @Override
    public EditMessageText showAnyNFLTeamInfo(Update update) {
        Team team = Team.valueOf(update.getCallbackQuery().getData());
        List<NFLTeam> nflTeams = apiService.getNFLTeamInfo();
        NFLTeam nflTeam = nflTeams.stream().filter(el -> el.getKey().equals(team)).findAny().get();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("City: ").append(nflTeam.getCity()).append("\n")
                .append("Name: ").append(nflTeam.getName()).append("\n")
                .append("Conference: ").append(nflTeam.getConference()).append("\n")
                .append("Division: ").append(nflTeam.getDivision()).append("\n")
                .append("Head Coach: ").append(nflTeam.getHeadCoach()).append("\n")
                .append("Offensive Coordinator: ").append(nflTeam.getOffensiveCoordinator()).append("\n")
                .append("Defensive Coordinator: ").append(nflTeam.getDefensiveCoordinator()).append("\n")
                .append("Special Teams Coach: ").append(nflTeam.getSpecialTeamsCoach()).append("\n");
        return botMessageService.createEditMessageText(update, stringBuilder.toString());
    }

}
