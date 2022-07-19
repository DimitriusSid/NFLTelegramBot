package nfl.telegram.bot.service.botService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface BotMessageService {

    SendMessage sendGreetingMessage(Update update);
    SendMessage sendSimpleMessage(Update update, String textMessage);
    SendMessage chooseFavoriteTeam(Update update);
    EditMessageText createEditMessageText(Update update, String textMessage);


}
