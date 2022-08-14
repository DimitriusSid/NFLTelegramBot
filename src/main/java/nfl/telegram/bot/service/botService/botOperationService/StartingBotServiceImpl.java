package nfl.telegram.bot.service.botService.botOperationService;

import nfl.telegram.bot.domian.BotUser;
import nfl.telegram.bot.domian.Team;
import nfl.telegram.bot.service.botService.botButtonService.BotButtonService;
import nfl.telegram.bot.service.botService.botMessageService.BotMessageService;
import nfl.telegram.bot.service.dataBaseService.dataServcie.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class StartingBotServiceImpl implements StartingBotService {

    @Value("${bot.message.greeting_message}")
    private String GREETING_MESSAGE;
    @Value("${bot.message.choose_team_message}")
    private String CHOOSE_TEAM_MESSAGE;
    @Value("${bot.message.selected_team_message}")
    private String SELECTED_TEAM_MESSAGE;

    private final DataService dataService;
    private final BotMessageService botMessageService;
    private final BotButtonService botButtonService;

    @Autowired
    public StartingBotServiceImpl(DataService dataService,
                                  BotMessageService botMessageService,
                                  BotButtonService botButtonService) {
        this.dataService = dataService;
        this.botMessageService = botMessageService;
        this.botButtonService = botButtonService;
    }

    @Override
    public SendMessage sendGreetingsMessage(Update update) {
        saveBotUser(update);
        return botMessageService.sendSimpleMessage(update, String.format(GREETING_MESSAGE,
                update.getMessage().getChat().getFirstName()));
    }

    @Override
    public SendMessage chooseFavoriteTeam(Update update) {
        SendMessage sendMessage = botMessageService.sendSimpleMessage(update, CHOOSE_TEAM_MESSAGE);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
    }

    @Override
    public EditMessageText sendMessageOfSelectedTeam(Update update) {
        saveBotUser(update);
        return botMessageService.sendEditMessageText(update, SELECTED_TEAM_MESSAGE);
    }

    @Override
    public void saveBotUser(Update update) {
        if (update.getMessage() != null) {
            Long userId = update.getMessage().getFrom().getId();
            dataService.saveBotUser(new BotUser(userId, null));
        } else {
            Long userId = update.getCallbackQuery().getFrom().getId();
            Team team = Team.valueOf(update.getCallbackQuery().getData());
            dataService.saveBotUser(new BotUser(userId, team));
        }
    }
}
