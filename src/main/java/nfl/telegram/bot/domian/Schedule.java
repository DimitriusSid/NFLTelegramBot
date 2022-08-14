package nfl.telegram.bot.domian;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class Schedule {

    @JsonProperty(value = "Week")
    private int week;

    @JsonProperty(value = "Date")
    private Date date;

    @JsonProperty(value = "HomeTeam")
    private String homeTeam;

    @JsonProperty(value = "AwayTeam")
    private String awayTeam;

}
