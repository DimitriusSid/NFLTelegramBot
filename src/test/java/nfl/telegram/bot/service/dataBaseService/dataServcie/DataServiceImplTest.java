package nfl.telegram.bot.service.dataBaseService.dataServcie;

import nfl.telegram.bot.domian.BotUser;
import nfl.telegram.bot.domian.Team;
import nfl.telegram.bot.service.dataBaseService.dataDAO.DataDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName(value = "Testing DataService")
@SpringBootTest
class DataServiceImplTest {

    @MockBean
    private DataDAO dataDAO;

    @Autowired
    private DataService dataService;

    @Test
    public void returnCorrectBotUser_WhenGettingBotUserByID() {
        Mockito.when(dataDAO.getBotUser(56L)).thenReturn(new BotUser(56L, Team.WAS));

        BotUser botUser = dataService.getBotUser(56L);
        Long id = botUser.getId();

        assertEquals(56L, id);
        Mockito.verify(dataDAO, Mockito.times(1)).getBotUser(56L);
    }

    @Test
    public void returnNull_WhenThereIsNoBotUserWithId() {
        Mockito.when(dataDAO.getBotUser(44L)).thenReturn(null);

        BotUser botUser = dataService.getBotUser(44L);

        assertNull(botUser);
        Mockito.verify(dataDAO, Mockito.times(1)).getBotUser(44L);
    }

    @Test
    public void returnBotUser_WhenBotUserSaved() {
        BotUser botUser = new BotUser(12L, Team.ATL);
        Mockito.when(dataDAO.saveBotUser(botUser)).thenReturn(botUser);

        BotUser savedBotUser = dataDAO.saveBotUser(new BotUser(12L, Team.ATL));

        assertEquals(botUser, savedBotUser);
        Mockito.verify(dataDAO, Mockito.times(1)).saveBotUser(botUser);
    }
}