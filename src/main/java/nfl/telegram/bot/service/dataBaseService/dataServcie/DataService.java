package nfl.telegram.bot.service.dataBaseService.dataServcie;

import nfl.telegram.bot.domian.BotUser;

public interface DataService {
    BotUser saveBotUser(BotUser botUser);
    BotUser getBotUser(Long userId);
}
