package nfl.telegram.bot.service.dataBaseService;

import nfl.telegram.bot.domian.BotUser;

public interface DataService {

    BotUser saveBotUser(BotUser botUser);
    BotUser getBotUser(Long userId);
}
