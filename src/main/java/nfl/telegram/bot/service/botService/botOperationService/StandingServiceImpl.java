package nfl.telegram.bot.service.botService.botOperationService;

import nfl.telegram.bot.domian.BotUser;
import nfl.telegram.bot.domian.Standing;
import nfl.telegram.bot.domian.Team;
import nfl.telegram.bot.service.botService.botButtonService.BotButtonService;
import nfl.telegram.bot.service.botService.botMessageService.BotMessageService;
import nfl.telegram.bot.service.dataBaseService.dataServcie.DataService;
import nfl.telegram.bot.service.nflApiService.ApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class StandingServiceImpl implements StandingService {

    @Value("${bot.message.standing_for_any_team_message}")
    private String STANDING_FOR_ANY_TEAM_MESSAGE;
    @Value("${bot.message.select_team_message}")
    private String SELECT_TEAM_MESSAGE;

    private final ApiService apiService;
    private final DataService dataService;
    private final BotMessageService botMessageService;
    private final BotButtonService botButtonService;

    public StandingServiceImpl(ApiService apiService,
                               DataService dataService,
                               BotMessageService botMessageService,
                               BotButtonService botButtonService) {
        this.apiService = apiService;
        this.dataService = dataService;
        this.botMessageService = botMessageService;
        this.botButtonService = botButtonService;
    }

    @Override
    public SendMessage showStandingForFavoriteTeam(Update update) {
        BotUser botUser = dataService.getBotUser(update.getMessage().getFrom().getId());
        Team team = botUser.getTeam();
        if (team == null) {
            return botMessageService.sendSimpleMessage(update, SELECT_TEAM_MESSAGE);
        } else {
            List<Standing> standings = apiService.getSeasonStanding();
            Standing standing = standings.stream().filter(s -> s.getTeam().equals(team)).findAny().orElseThrow();
            String message = getStandingAsStringMessage(standing);
            return botMessageService.sendSimpleMessage(update, message);
        }
    }

    @Override
    public SendMessage chooseAnyTeamForStanding(Update update) {
        SendMessage sendMessage = botMessageService.sendSimpleMessage(update, STANDING_FOR_ANY_TEAM_MESSAGE);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
    }

    @Override
    public EditMessageText showStandingForAnyTeam(Update update) {
        Team team = Team.valueOf(update.getCallbackQuery().getData());
        List<Standing> standings = apiService.getSeasonStanding();
        Standing standing = standings.stream().filter(s -> s.getTeam().equals(team)).findAny().orElseThrow();
        String message = getStandingAsStringMessage(standing);
        return botMessageService.sendEditMessageText(update, message);
    }

    private String getStandingAsStringMessage(Standing standing) {
        return "Team: " + standing.getTeam() + "\n" +
                "Conference: " + standing.getConference() + "\n" +
                "Division: " + standing.getDivision() + "\n" +
                "Wins: " + standing.getWins() + "\n" +
                "Losses: " + standing.getLosses() + "\n" +
                "Ties: " + standing.getTies() + "\n" +
                "Touchdowns: " + standing.getTouchdowns() + "\n" +
                "DivisionRank: " + standing.getDivisionRank() + "\n" +
                "ConferenceRank: " + standing.getConferenceRank() + "\n";
    }
}
