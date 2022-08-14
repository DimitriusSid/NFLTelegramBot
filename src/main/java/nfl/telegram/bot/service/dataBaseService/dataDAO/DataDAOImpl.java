package nfl.telegram.bot.service.dataBaseService.dataDAO;

import nfl.telegram.bot.domian.BotUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DataDAOImpl implements DataDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public DataDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveBotUser(BotUser botUser) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(botUser);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public BotUser getBotUser(Long userId) {
        Session session = sessionFactory.openSession();
        BotUser botUser;
        if (session.get(BotUser.class, userId) != null) {
            botUser = session.get(BotUser.class, userId);
        } else botUser = null;
        session.close();
        return botUser;
    }
}
