import java.util.ArrayList;
import java.util.List;

public class Match {

    private Team team1;
    private Team team2;

    public Match(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public List<String> playersInMatch() {
        List<String> players = new ArrayList<>();

        players.addAll(team1.getPlayers());
        players.addAll(team2.getPlayers());

        return players;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String p : playersInMatch()) {
            sb.append(p);
            sb.append(":");
        }

        String s = sb.toString();

        return s.substring(0, s.length() - 1);
    }
}
