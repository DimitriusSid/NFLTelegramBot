package nfl.telegram.bot.service.botService.botOperationService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ScheduleService {
    SendMessage showCurrentWeek(Update update);
    SendMessage showScheduleForCurrentWeek(Update update);
    SendMessage showScheduleForFavoriteTeam(Update update);
    SendMessage chooseAnyTeamForSchedule(Update update);
    EditMessageText showScheduleForAnyTeam(Update update);
}
