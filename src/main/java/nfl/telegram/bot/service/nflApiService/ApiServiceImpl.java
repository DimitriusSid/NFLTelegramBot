package nfl.telegram.bot.service.nflApiService;

import nfl.telegram.bot.domian.ByeWeek;
import nfl.telegram.bot.domian.NFLTeam;
import nfl.telegram.bot.domian.Schedule;
import nfl.telegram.bot.domian.Standing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ApiServiceImpl implements ApiService {

    @Value("${nfl.api.url.byes}")
    private String NFL_API_URL_BYES;

    @Value("${nfl.api.url.current_week}")
    private String NFL_API_URL_CURRENT_WEEK;

    @Value("${nfl.api.url.schedule}")
    private String NFL_API_URL_SCHEDULE;

    @Value("${nfl.api.url.standing}")
    private String NFL_API_URL_STANDING;

    @Value("${nfl.api.url.nfl_team_info}")
    private String NFL_API_URL_NFL_TEAM_INFO;

    @Value("${nfl.api.key}")
    private String NFL_API_KEY;




    private final RestTemplate restTemplate;

    @Autowired
    public ApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ByeWeek> getByeWeeks() {
        ResponseEntity<ByeWeek[]> responseEntity =
                restTemplate.getForEntity(NFL_API_URL_BYES + NFL_API_KEY, ByeWeek[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public String getCurrentWeek() {
        return restTemplate.getForObject(NFL_API_URL_CURRENT_WEEK + NFL_API_KEY, String.class);
    }


    @Override
    public List<Schedule> getSeasonSchedule() {
        ResponseEntity<Schedule[]> responseEntity =
                restTemplate.getForEntity(NFL_API_URL_SCHEDULE + NFL_API_KEY, Schedule[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public List<Standing> getSeasonStanding() {
        ResponseEntity<Standing[]> responseEntity =
                restTemplate.getForEntity(NFL_API_URL_STANDING + NFL_API_KEY, Standing[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public List<NFLTeam> getNFLTeamInfo() {
        ResponseEntity<NFLTeam[]> responseEntity =
                restTemplate.getForEntity(NFL_API_URL_NFL_TEAM_INFO + NFL_API_KEY, NFLTeam[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }
}
