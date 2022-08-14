package nfl.telegram.bot.service.botService;

import nfl.telegram.bot.service.botService.botMessageService.BotMessageService;
import nfl.telegram.bot.service.botService.botOperationService.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import static nfl.telegram.bot.constant.BotCommand.*;
import static nfl.telegram.bot.constant.BotCommand.ANY_NFL_TEAM_INFO;

@Service
public class BotUpdateServiceImpl implements BotUpdateService {

    @Value("${bot.name}")
    private String BOT_NAME;
    @Value("${bot.token}")
    private String TOKEN;
    @Value("${bot.message.incorrect_value}")
    private String INCORRECT_VALUE_MESSAGE;
    @Value("${bot.message.choose_team_message}")
    private String CHOOSE_TEAM_MESSAGE;
    @Value("${bot.message.schedule_for_any_team_message}")
    private String TEAM_SCHEDULE;
    @Value("${bot.message.bye_week_for_team_message}")
    private String BYEWEEK_FOR_TEAM;
    @Value("${bot.message.standing_for_any_team_message}")
    private String STANDING_FOR_TEAM;
    @Value("${bot.message.nfl_any_team_info_message}")
    private String NFL_TEAM_INFO;

    private final BotMessageService botMessageService;
    private final StartingBotService startingBotService;
    private final ByeWeeksService byeWeeksService;
    private final ScheduleService scheduleService;
    private final StandingService standingService;
    private final NFLTeamInfoService nflTeamInfoService;

    public BotUpdateServiceImpl(BotMessageService botMessageService,
                                StartingBotService startingBotService,
                                ByeWeeksService byeWeeksService,
                                ScheduleService scheduleService,
                                StandingService standingService,
                                NFLTeamInfoService nflTeamInfoService) {
        this.botMessageService = botMessageService;
        this.startingBotService = startingBotService;
        this.byeWeeksService = byeWeeksService;
        this.scheduleService = scheduleService;
        this.standingService = standingService;
        this.nflTeamInfoService = nflTeamInfoService;
    }


    @Override
    public SendMessage updateHandler(Update update) {
        String incomingTextMessage = update.getMessage().getText();
        switch (incomingTextMessage) {
            case START:
                return startingBotService.sendGreetingsMessage(update);
            case CHOOSE_FAVORITE_TEAM:
            case CHANGE_FAVORITE_TEAM:
                return startingBotService.chooseFavoriteTeam(update);
            case BYE_WEEKS:
                return byeWeeksService.showByeWeeks(update);
            case BYE_WEEK_USER_TEAM:
                return byeWeeksService.showByeWeekForFavoriteTeam(update);
            case BYE_WEEK_FOR_ANY_TEAM:
                return byeWeeksService.chooseAnyTeamForByeWeek(update);
            case CURRENT_WEEK:
                return scheduleService.showCurrentWeek(update);
            case SCHEDULE_FOR_USER_TEAM:
                return scheduleService.showScheduleForFavoriteTeam(update);
            case CURRENT_WEEK_SCHEDULE:
                return scheduleService.showScheduleForCurrentWeek(update);
            case SCHEDULE_FOR_TEAM:
                return scheduleService.chooseAnyTeamForSchedule(update);
            case STANDING_FOR_USER_TEAM:
                return standingService.showStandingForFavoriteTeam(update);
            case STANDING_FOR_ANY_TEAM:
                return standingService.chooseAnyTeamForStanding(update);
            case USER_NFL_TEAM_INFO:
                return nflTeamInfoService.showFavoriteNFLTeamInfo(update);
            case ANY_NFL_TEAM_INFO:
                return nflTeamInfoService.chooseAnyNFLTeamForInfo(update);
            default:
                return botMessageService.sendSimpleMessage(update, INCORRECT_VALUE_MESSAGE);
        }
    }

    @Override
    public EditMessageText callBackQueryHandler(Update update) {
        if (update.getCallbackQuery().getMessage().getText().equals(CHOOSE_TEAM_MESSAGE)) {
            return startingBotService.sendMessageOfSelectedTeam(update);
        } else if (update.getCallbackQuery().getMessage().getText().equals(TEAM_SCHEDULE)) {
            return scheduleService.showScheduleForAnyTeam(update);
        } else if (update.getCallbackQuery().getMessage().getText().equals(BYEWEEK_FOR_TEAM)) {
            return byeWeeksService.showByeWeekForAnyTeam(update);
        } else if (update.getCallbackQuery().getMessage().getText().equals(STANDING_FOR_TEAM)) {
            return standingService.showStandingForAnyTeam(update);
        } else if (update.getCallbackQuery().getMessage().getText().equals(NFL_TEAM_INFO)) {
            return nflTeamInfoService.showAnyNFLTeamInfo(update);
        }
        return botMessageService.sendEditMessageText(update, INCORRECT_VALUE_MESSAGE);
    }


}
