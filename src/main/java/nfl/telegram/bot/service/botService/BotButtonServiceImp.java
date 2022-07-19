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
    public InlineKeyboardMarkup createInlineKeyboardMarkupForChoosingTeam() {

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        for (Team team : Team.values()) {
            buttons.add(InlineKeyboardButton.builder()
                    .text(String.valueOf(team))
                    .callbackData(String.valueOf(team))
                    .build());
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(buttons.subList(0, 8))
                .keyboardRow(buttons.subList(8, 16))
                .keyboardRow(buttons.subList(16, 24))
                .keyboardRow(buttons.subList(24, 32))
                .build();

        return inlineKeyboardMarkup;
    }



}
