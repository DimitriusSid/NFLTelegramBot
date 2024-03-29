package nfl.telegram.bot.service.botService.botButtonService;

import nfl.telegram.bot.domian.Team;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class BotButtonServiceImp implements BotButtonService {

    @Override
    public InlineKeyboardMarkup createTeamInlineKeyboardMarkup() {
        List<InlineKeyboardButton> teamInlineButtons = createTeamInlineKeyboardButtonList();
        return InlineKeyboardMarkup.builder()
                .keyboardRow(teamInlineButtons.subList(0, 4))
                .keyboardRow(teamInlineButtons.subList(4, 8))
                .keyboardRow(teamInlineButtons.subList(8, 12))
                .keyboardRow(teamInlineButtons.subList(12, 16))
                .keyboardRow(teamInlineButtons.subList(16, 20))
                .keyboardRow(teamInlineButtons.subList(20, 24))
                .keyboardRow(teamInlineButtons.subList(24, 28))
                .keyboardRow(teamInlineButtons.subList(28, 32))
                .build();
    }

    private List<InlineKeyboardButton> createTeamInlineKeyboardButtonList() {
        List<InlineKeyboardButton> teamInlineButtons = new ArrayList<>();
        for (Team team : Team.values()) {
            teamInlineButtons.add(InlineKeyboardButton.builder()
                    .text(String.valueOf(team))
                    .callbackData(String.valueOf(team))
                    .build());
        }
        return teamInlineButtons;
    }
}
