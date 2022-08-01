package nfl.telegram.bot.domian;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ByeWeek {

    @JsonProperty(value = "Season")
    private String season;

    @JsonProperty(value = "Week")
    private String week;

    @JsonProperty(value = "Team")
    private Team team;

}
