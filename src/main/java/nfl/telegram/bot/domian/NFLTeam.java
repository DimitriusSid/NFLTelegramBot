package nfl.telegram.bot.domian;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NFLTeam {

    @JsonProperty(value = "Key")
    private Team key;

    @JsonProperty(value = "City")
    private String city;

    @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "Conference")
    private String conference;

    @JsonProperty(value = "Division")
    private String division;

    @JsonProperty(value = "HeadCoach")
    private String headCoach;

    @JsonProperty(value = "OffensiveCoordinator")
    private String offensiveCoordinator;

    @JsonProperty(value = "DefensiveCoordinator")
    private String defensiveCoordinator;

    @JsonProperty(value = "SpecialTeamsCoach")
    private String specialTeamsCoach;
}
