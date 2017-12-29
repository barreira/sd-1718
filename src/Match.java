import java.util.ArrayList;
import java.util.List;

public class Match {

    private Team team1;
    private Team team2;

    public Match(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
    }

    public List<Player> playersInMatch() {
        List<Player> players = new ArrayList<>();

        players.addAll(team1.getPlayers());
        players.addAll(team2.getPlayers());

        return players;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Player p : playersInMatch()) {
            sb.append(p.getUsername());
            sb.append(":");
        }

        String s = sb.toString();

        return s.substring(0, s.length() - 1);
    }
}
