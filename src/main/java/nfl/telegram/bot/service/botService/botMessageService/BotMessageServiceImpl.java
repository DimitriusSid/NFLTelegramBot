package nfl.telegram.bot.service.botService.botMessageService;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@PropertySource("classpath:application.properties")
public class BotMessageServiceImpl implements BotMessageService {

    @Override
    public SendMessage sendSimpleMessage(Update update, String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText(textMessage);
        return sendMessage;
    }

    @Override
    public EditMessageText sendEditMessageText(Update update, String textMessage) {
        return EditMessageText.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text(String.format(textMessage, update.getCallbackQuery().getData()))
                .build();
    }
}
