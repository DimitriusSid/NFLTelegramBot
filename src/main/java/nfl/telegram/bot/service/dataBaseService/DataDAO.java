package nfl.telegram.bot.service.dataBaseService;

import nfl.telegram.bot.domian.BotUser;

public interface DataDAO {

    BotUser saveBotUser(BotUser botUser);
    BotUser getBotUser(Long userId);
}
