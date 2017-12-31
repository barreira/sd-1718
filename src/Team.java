import java.util.List;
import java.util.Map;

public class Team {

    private List<String> players;

    private static Map<Player, String> selectedHeroes; // hero <-> player
    private static final String[] heroes = {"Ana", "Bastion", "D.Va"};

    public Team(List<String> players) {
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }
}