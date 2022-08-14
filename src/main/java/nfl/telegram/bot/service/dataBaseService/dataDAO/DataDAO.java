package nfl.telegram.bot.service.dataBaseService.dataDAO;

import nfl.telegram.bot.domian.BotUser;

public interface DataDAO {
    void saveBotUser(BotUser botUser);
    BotUser getBotUser(Long userId);
}
