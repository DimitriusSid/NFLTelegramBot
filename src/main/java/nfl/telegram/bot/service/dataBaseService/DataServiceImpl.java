package nfl.telegram.bot.service.dataBaseService;

import nfl.telegram.bot.domian.BotUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataServiceImpl implements DataService {

    private final DataDAO dataDAO;

    @Autowired
    public DataServiceImpl(DataDAO dataDAO) {
        this.dataDAO = dataDAO;
    }

    @Override
    public BotUser saveBotUser(BotUser botUser) {
        return dataDAO.saveBotUser(botUser);
    }

    @Override
    public BotUser getBotUser(Long userId) {
        return dataDAO.getBotUser(userId);
    }

}
