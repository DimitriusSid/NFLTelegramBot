package nfl.telegram.bot.service.botService.botOperationService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface StandingService {
    SendMessage showStandingForFavoriteTeam(Update update);
    SendMessage chooseAnyTeamForStanding(Update update);
    EditMessageText showStandingForAnyTeam(Update update);
}
