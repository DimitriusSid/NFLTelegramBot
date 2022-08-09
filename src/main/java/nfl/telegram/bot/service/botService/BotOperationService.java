package nfl.telegram.bot.service.botService;

import nfl.telegram.bot.domian.BotUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotOperationService {

    SendMessage chooseFavoriteTeam(Update update);
    SendMessage sendGreetingsMessage(Update update);
    EditMessageText sendMessageOfSelectedTeam(Update update);
    SendMessage showByeWeeks(Update update);
    SendMessage showByeWeekForFavoriteTeam(Update update);
    SendMessage showCurrentWeek(Update update);
    SendMessage showScheduleForFavoriteTeam(Update update);
    SendMessage showScheduleForCurrentWeek(Update update);

    SendMessage chooseScheduleTeam(Update update);
    EditMessageText showScheduleForTeam(Update update);

    BotUser saveBotUser(Update update);


}
