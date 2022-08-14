package nfl.telegram.bot.service.botService.botOperationService;

import nfl.telegram.bot.domian.BotUser;
import nfl.telegram.bot.domian.NFLTeam;
import nfl.telegram.bot.domian.Team;
import nfl.telegram.bot.service.botService.botButtonService.BotButtonService;
import nfl.telegram.bot.service.botService.botMessageService.BotMessageService;
import nfl.telegram.bot.service.dataBaseService.dataServcie.DataService;
import nfl.telegram.bot.service.nflApiService.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class NFLTeamInfoServiceImpl implements NFLTeamInfoService {

    @Value("${bot.message.nfl_any_team_info_message}")
    private String NFL_ANY_TEAM_INFO_MESSAGE;
    @Value("${bot.message.select_team_message}")
    private String SELECT_TEAM_MESSAGE;

    private final BotMessageService botMessageService;
    private final BotButtonService botButtonService;
    private final DataService dataService;
    private final ApiService apiService;

    @Autowired
    public NFLTeamInfoServiceImpl(BotMessageService botMessageService,
                                  BotButtonService botButtonService,
                                  DataService dataService,
                                  ApiService apiService) {
        this.botMessageService = botMessageService;
        this.botButtonService = botButtonService;
        this.dataService = dataService;
        this.apiService = apiService;
    }

    @Override
    public SendMessage showFavoriteNFLTeamInfo(Update update) {
        BotUser botUser = dataService.getBotUser(update.getMessage().getFrom().getId());
        Team team = botUser.getTeam();
        if (team == null) {
            return botMessageService.sendSimpleMessage(update, SELECT_TEAM_MESSAGE);
        } else {
            List<NFLTeam> nflTeams = apiService.getNFLTeamInfo();
            NFLTeam nflTeam = nflTeams.stream().filter(el -> el.getKey().equals(team)).findAny().orElseThrow();
            String message = getNFLTeamAsStringMessage(nflTeam);
            return botMessageService.sendSimpleMessage(update, message);
        }
    }

    @Override
    public SendMessage chooseAnyNFLTeamForInfo(Update update) {
        SendMessage sendMessage = botMessageService.sendSimpleMessage(update, NFL_ANY_TEAM_INFO_MESSAGE);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
    }

    @Override
    public EditMessageText showAnyNFLTeamInfo(Update update) {
        Team team = Team.valueOf(update.getCallbackQuery().getData());
        List<NFLTeam> nflTeams = apiService.getNFLTeamInfo();
        NFLTeam nflTeam = nflTeams.stream().filter(el -> el.getKey().equals(team)).findAny().orElseThrow();
        String message = getNFLTeamAsStringMessage(nflTeam);
        return botMessageService.sendEditMessageText(update, message);
    }

    private String getNFLTeamAsStringMessage(NFLTeam nflTeam) {
        return "City: " + nflTeam.getCity() + "\n" +
                "Name: " + nflTeam.getName() + "\n" +
                "Conference: " + nflTeam.getConference() + "\n" +
                "Division: " + nflTeam.getDivision() + "\n" +
                "Head Coach: " + nflTeam.getHeadCoach() + "\n" +
                "Offensive Coordinator: " + nflTeam.getOffensiveCoordinator() + "\n" +
                "Defensive Coordinator: " + nflTeam.getDefensiveCoordinator() + "\n" +
                "Special Teams Coach: " + nflTeam.getSpecialTeamsCoach() + "\n";
    }
}
