import java.util.List;
import java.util.Map;

public class Team {

    private List<Player> players;

    private static Map<Player, String> selectedHeroes; // hero <-> player
    private static final String[] heroes = {"Ana", "Bastion", "D.Va"};

    public Team(List<Player> players) {
        this.players = players;
    }
}