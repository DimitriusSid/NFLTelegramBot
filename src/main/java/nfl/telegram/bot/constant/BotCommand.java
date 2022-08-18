package nfl.telegram.bot.constant;

import org.springframework.stereotype.Component;

@Component
public class BotCommand {

    // starting boot
    public static final String START = "/start";

    // choosing team
    public static final String CHOOSE_FAVORITE_TEAM = "/chooseteam";
    public static final String CHANGE_FAVORITE_TEAM = "/changefavoriteteam";

    // byeWeek
    public static final String BYE_WEEKS = "/byeweeks";
    public static final String BYE_WEEK_USER_TEAM = "/byeweekuserteam";
    public static final String BYE_WEEK_FOR_ANY_TEAM = "/byeweekanyteam";

    // currentTeam
    public static final String CURRENT_WEEK = "/currentweek";

    // Schedule
    public static final String SCHEDULE_FOR_USER_TEAM = "/scheduleuserteam";
    public static final String SCHEDULE_FOR_TEAM = "/scheduleforanyteam";
    public static final String CURRENT_WEEK_SCHEDULE = "/scheduleweek";

    // Standing
    public static final String STANDING_FOR_USER_TEAM = "/standinguserteam";
    public static final String STANDING_FOR_ANY_TEAM = "/standinganyteam";

    // NFLTeam
    public static final String USER_NFL_TEAM_INFO = "/userteaminfo";
    public static final String ANY_NFL_TEAM_INFO = "/anyteaminfo";
}
