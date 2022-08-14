package nfl.telegram.bot.service.botService.botOperationService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface StartingBotService {
    SendMessage sendGreetingsMessage(Update update);
    SendMessage chooseFavoriteTeam(Update update);
    EditMessageText sendMessageOfSelectedTeam(Update update);
    void saveBotUser(Update update);
}
