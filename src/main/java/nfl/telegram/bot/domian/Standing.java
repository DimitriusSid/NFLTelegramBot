package nfl.telegram.bot.domian;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Standing {

    @JsonProperty(value = "Conference")
    private String conference;

    @JsonProperty(value = "Division")
    private String division;

    @JsonProperty(value = "Team")
    private Team team;

    @JsonProperty(value = "Wins")
    private int wins;

    @JsonProperty(value = "Losses")
    private int losses;

    @JsonProperty(value = "Touchdowns")
    private int touchdowns;

    @JsonProperty(value = "DivisionRank")
    private int divisionRank;

    @JsonProperty(value = "ConferenceRank")
    private int conferenceRank;

}
