package nfl.telegram.bot.service.botService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class BotMessageServiceImpl implements BotMessageService {

    private BotButtonService botButtonService;

    @Autowired
    public BotMessageServiceImpl(BotButtonService botButtonService) {
        this.botButtonService = botButtonService;
    }

    @Override
    public SendMessage sendGreetingMessage(Update update) {
        return sendSimpleMessage(update, String.format("Hi, %s, you are welcome to NFL Bot. " +
                "Press or type /chooseTeam to choose your favorite NFL Team" ,
                update.getMessage().getChat().getFirstName()));
    }

    @Override
    public SendMessage chooseFavoriteTeam(Update update) {
        SendMessage sendMessage = sendSimpleMessage(update, "Select your team. You can always change" +
                " your favorite team. Just press or type \n/changeFavoriteTeam");
        sendMessage.setReplyMarkup(botButtonService.createInlineKeyboardMarkupForChoosingTeam());
        return sendMessage;
    }

    @Override
    public SendMessage sendSimpleMessage(Update update, String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText(textMessage);
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
