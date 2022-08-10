package nfl.telegram.bot.service.nflApiService;

import nfl.telegram.bot.domian.ByeWeek;
import nfl.telegram.bot.domian.NFLTeam;
import nfl.telegram.bot.domian.Schedule;
import nfl.telegram.bot.domian.Standing;

import java.util.List;

public interface ApiService {

    List<ByeWeek> getByeWeeks();
    String getCurrentWeek();
    List<Schedule> getSeasonSchedule();
    List<Standing> getSeasonStanding();
    List<NFLTeam> getNFLTeamInfo();

}
