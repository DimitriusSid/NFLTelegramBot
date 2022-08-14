package nfl.telegram.bot.service.botService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotUpdateService {
    SendMessage updateHandler(Update update);
    EditMessageText callBackQueryHandler(Update update);
}
