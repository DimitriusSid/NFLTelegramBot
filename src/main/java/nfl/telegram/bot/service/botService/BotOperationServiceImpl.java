package nfl.telegram.bot.service.botService;

import nfl.telegram.bot.domian.BotUser;
import nfl.telegram.bot.domian.Team;
import nfl.telegram.bot.service.dataBaseService.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    @Autowired
    public BotOperationServiceImpl(BotMessageService botMessageService, BotButtonService botButtonService, DataService dataService) {
        this.botMessageService = botMessageService;
        this.botButtonService = botButtonService;
        this.dataService = dataService;
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
