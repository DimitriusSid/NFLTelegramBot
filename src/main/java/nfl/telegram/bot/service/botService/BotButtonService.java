package nfl.telegram.bot.service.botService;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface BotButtonService {

    InlineKeyboardMarkup createInlineKeyboardMarkupForChoosingTeam();
}
