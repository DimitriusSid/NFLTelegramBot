package nfl.telegram.bot.service.botService.botOperationService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface NFLTeamInfoService {
    SendMessage showFavoriteNFLTeamInfo(Update update);
    SendMessage chooseAnyNFLTeamForInfo(Update update);
    EditMessageText showAnyNFLTeamInfo(Update update);
}
