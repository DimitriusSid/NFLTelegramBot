package nfl.telegram.bot.service.botService.botOperationService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ByeWeeksService {
    SendMessage showByeWeeks(Update update);
    SendMessage showByeWeekForFavoriteTeam(Update update);
    SendMessage chooseAnyTeamForByeWeek(Update update);
    EditMessageText showByeWeekForAnyTeam(Update update);
}
