package nfl.telegram.bot.service.nflApiService;

import nfl.telegram.bot.domian.BotUser;
import nfl.telegram.bot.domian.ByeWeek;
import nfl.telegram.bot.domian.Team;
import nfl.telegram.bot.service.dataBaseService.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApiServiceImpl implements ApiService {

    @Value("${nfl.api.url.byes}")
    private String NFL_API_URL_BYES;

    @Value("${nfl.api.key}")
    private String NFL_API_KEY;

    private final RestTemplate restTemplate;
    private final DataService dataService;

    @Autowired
    public ApiServiceImpl(RestTemplate restTemplate, DataService dataService) {
        this.restTemplate = restTemplate;
        this.dataService = dataService;
    }

    @Override
    public List<ByeWeek> getByeWeeks() {
        ResponseEntity<ByeWeek[]> responseEntity =
                restTemplate.getForEntity(NFL_API_URL_BYES + NFL_API_KEY, ByeWeek[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public ByeWeek getByeWeekForTeam(Update update) {
        List<ByeWeek> byeWeeks = getByeWeeks();
        Optional<ByeWeek> optionalByeWeek = byeWeeks.stream()
                .filter(byeWeek -> byeWeek.getTeam().equals(getBotUserTeam(update))).findAny();
        return optionalByeWeek.get();
    }

    private String createStringResult(List<ByeWeek> byeWeeks) {
        StringBuilder stringBuilder = new StringBuilder();
        byeWeeks.forEach(byeWeek -> stringBuilder.append("Team ")
                .append(byeWeek.getTeam())
                .append(" on week ")
                .append(byeWeek.getWeek()));
        return stringBuilder.toString();

    }

    private Team getBotUserTeam(Update update) {
        return dataService.getBotUser(update.getMessage().getFrom().getId()).getTeam();
    }
}
