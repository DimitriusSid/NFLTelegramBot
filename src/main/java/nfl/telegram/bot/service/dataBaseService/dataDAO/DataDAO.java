package nfl.telegram.bot.service.dataBaseService.dataDAO;

import nfl.telegram.bot.domian.BotUser;

public interface DataDAO {
    BotUser saveBotUser(BotUser botUser);
    BotUser getBotUser(Long userId);
}
