package nfl.telegram.bot.service.dataBaseService.dataServcie;

import nfl.telegram.bot.domian.BotUser;
import nfl.telegram.bot.service.dataBaseService.dataDAO.DataDAO;
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
    public void saveBotUser(BotUser botUser) {
        dataDAO.saveBotUser(botUser);
    }

    @Override
    public BotUser getBotUser(Long userId) {
        return dataDAO.getBotUser(userId);
    }

}
