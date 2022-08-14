package nfl.telegram.bot.service.botService.botMessageService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface BotMessageService {
    SendMessage sendSimpleMessage(Update update, String textMessage);
    EditMessageText sendEditMessageText(Update update, String textMessage);
}
