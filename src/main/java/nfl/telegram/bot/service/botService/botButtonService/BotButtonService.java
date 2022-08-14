package nfl.telegram.bot.service.botService.botButtonService;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface BotButtonService {
    InlineKeyboardMarkup createTeamInlineKeyboardMarkup();
}
