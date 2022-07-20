package nfl.telegram.bot.service.botService;

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
                .keyboardRow(teamInlineButtons.subList(0, 8))
                .keyboardRow(teamInlineButtons.subList(9, 16))
                .keyboardRow(teamInlineButtons.subList(16, 24))
                .keyboardRow(teamInlineButtons.subList(24, 32))
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
