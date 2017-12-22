import java.util.List;

public class Match {

    private Team team1;
    private Team team2;

    public Match(List<Player> team1, List<Player> team2) {
        this.team1 = new Team(team1);
        this.team2 = new Team(team2);
    }
}
