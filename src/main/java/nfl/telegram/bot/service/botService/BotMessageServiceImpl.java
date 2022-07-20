package nfl.telegram.bot.service.botService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@PropertySource("classpath:application.properties")
public class BotMessageServiceImpl implements BotMessageService {

    @Value("${bot.message.greeting}")
    private String GREETING_MESSAGE;
    @Value("${bot.message.choose_team}")
    private String CHOOSE_TEAM_MESSAGE;

    private BotButtonService botButtonService;

    @Autowired
    public BotMessageServiceImpl(BotButtonService botButtonService) {
        this.botButtonService = botButtonService;
    }

    @Override
    public SendMessage sendSimpleMessage(Update update, String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText(textMessage);
        return sendMessage;
    }

    @Override
    public SendMessage sendGreetingMessage(Update update) {
        if (GREETING_MESSAGE == null) System.out.println(0);
        return sendSimpleMessage(update, String.format(GREETING_MESSAGE, update.getMessage().getChat().getFirstName()));
    }

    @Override
    public SendMessage chooseFavoriteTeam(Update update) {
        SendMessage sendMessage = sendSimpleMessage(update, CHOOSE_TEAM_MESSAGE);
        sendMessage.setReplyMarkup(botButtonService.createTeamInlineKeyboardMarkup());
        return sendMessage;
    }


    @Override
    public EditMessageText createEditMessageText(Update update, String textMessage) {
        EditMessageText editMessageText = EditMessageText.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text(String.format("You've selected %s",
                        update.getCallbackQuery().getData()))
                .build();
        return editMessageText;
    }
}
