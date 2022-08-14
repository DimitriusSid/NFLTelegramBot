package nfl.telegram.bot.service.botService.botOperationService;

import nfl.telegram.bot.domian.ByeWeek;
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
public class ByeWeeksServiceImp implements ByeWeeksService {

    @Value("${bot.message.bye_week_for_team_message}")
    private String BYE_WEEK_FOR_TEAM_MESSAGE;
    @Value("${bot.message.select_team_message}")
    private String SELECT_TEAM_MESSAGE;

    private final ApiService apiService;
    private final BotMessageService botMessageService;
    private final DataService dataService;
    private final BotButtonService botButtonService;

    @Autowired
    public ByeWeeksServiceImp(ApiService apiService,
                              BotMessageService botMessageService,
                              DataService dataService,
                              BotButtonService botButtonService) {
        this.apiService = apiService;
        this.botMessageService = botMessageService;
        this.dataService = dataService;
        this.botButtonService = botButtonService;
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
        Team team = dataService.getBotUser(update.getMessage().getFrom().getId()).getTeam();
        List<ByeWeek> byeWeeks = apiService.getByeWeeks();
        ByeWeek byeWeek = byeWeeks.stream().filter(bw -> bw.getTeam().equals(team)).findAny().orElse(null);
        if (byeWeek == null) {
            return botMessageService.sendSimpleMessage(update, SELECT_TEAM_MESSAGE);
        } else {
            String message = "Team " + byeWeek.getTeam() + " on week " + byeWeek.getWeek();
            return botMessageService.sendSimpleMessage(update, message);
        }
    }

    @Override
    public SendMessage chooseAnyTeamForByeWeek(Update update) {
        SendMessage sendMessage = botMessageService.sendSimpleMessage(update, BYE_WEEK_FOR_TEAM_MESSAGE);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
    }

    @Override
    public EditMessageText showByeWeekForAnyTeam(Update update) {
        Team team = Team.valueOf(update.getCallbackQuery().getData());
        List<ByeWeek> byeWeeks = apiService.getByeWeeks();
        ByeWeek byeWeek = byeWeeks.stream().filter(b -> b.getTeam().equals(team)).findAny().orElseThrow();
        String message = "Team " + byeWeek.getTeam() + " on week " + byeWeek.getWeek();
        return botMessageService.sendEditMessageText(update, message);
    }


}
