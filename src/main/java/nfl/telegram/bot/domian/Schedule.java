package nfl.telegram.bot.domian;

import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

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


    @Data
    @JsonRootName(value = "StadiumDetails")
    public static class Stadium {
        @JsonProperty(value = "name")
        private String name;

        @JsonProperty(value = "City")
        private String city;

    }




}
