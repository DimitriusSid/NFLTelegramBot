package nfl.telegram.bot.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "bot_users")
public class BotUser {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "team")
    @Enumerated(EnumType.STRING)
    private Team team;


}
