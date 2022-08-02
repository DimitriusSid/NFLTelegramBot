package nfl.telegram.bot.service.nflApiService;

import nfl.telegram.bot.domian.ByeWeek;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface ApiService {

    List<ByeWeek> getByeWeeks();
    ByeWeek getByeWeekForTeam(Update update);
    String getCurrentWeek();

}
