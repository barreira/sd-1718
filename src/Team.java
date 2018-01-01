import java.util.List;
import java.util.Map;

public class Team {

    private List<String> players;

    private static Map<Player, String> selectedHeroes; // hero <-> player
    private static final String[] heroes = {"Ana", "Bastion", "D.Va", "Doomfist", "Genji", "Hanzo", "Junkrat", "Lúcio",
                                            "McCree", "Mei", "Mercy", "Orisa", "Pharah", "Reaper", "Reinhardt",
                                            "Roadhog", "Soldier: 76", "Sombra", "Symmetra", "Torbjorn", "Tracer",
                                            "Widowmaker", "Winston", "Zarya", "Zenyatta"};

    public Team(List<String> players) {
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }
}