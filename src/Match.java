import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Match {

    private Team team1;
    private Team team2;

    private final Lock locker;

    public Match(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
        locker = new ReentrantLock();
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

    public Map<String, String> selectedHeroes(int team) {
        return (team == 1) ? team1.getSelectedHeroes() : team2.getSelectedHeroes();
    }

    private int playerTeam(String player) {
        return (team1.getPlayers().contains(player)) ? 1 : 2;
    }

    public boolean selectHero(String player, String hero) {
        locker.lock();

        try {
            int team = playerTeam(player);

            return (team == 1) ? team1.selectHero(player, hero) : team2.selectHero(player, hero);
        }
        finally {
            locker.unlock();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(team1.toString());
        sb.append("|");
        sb.append(team2.toString());

        return sb.toString();
    }
}
