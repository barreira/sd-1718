import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class Match {

    private int id;
    private Team team1;
    private Team team2;
    private boolean closed;
    private final Lock locker;


    Match(int id, Team team1, Team team2) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        closed = false;
        locker = new ReentrantLock();
    }


    int getID() {
        return id;
    }


    Team getTeam1() {
        return team1;
    }


    Team getTeam2() {
        return team2;
    }


    void assignHeroes() {
        this.assignHeroes(team1.getSelectedHeroes());
        this.assignHeroes(team2.getSelectedHeroes());
    }


    private void assignHeroes(Map<String, String> team) {
        for (String p : team.keySet()) {
            if (team.get(p).equals("")) {
                for (String hero : Overwatch.heroes) {
                    if (!team.values().contains(hero)) {
                        team.put(p, hero);
                        break;
                    }
                }
            }
        }
    }


    /*List<String> playersInMatch() {
        List<String> players = new ArrayList<>();

        players.addAll(team1.getPlayers());
        players.addAll(team2.getPlayers());

        return players;
    }


    Map<String, String> selectedHeroes(int team) {
        return (team == 1) ? team1.getSelectedHeroes() : team2.getSelectedHeroes();
    }*/


    private int playerTeam(String player) {
        return (team1.getPlayers().contains(player)) ? 1 : 2;
    }


    boolean selectHero(String player, String hero) {
        locker.lock();

        try {
            int team = playerTeam(player);

            return (team == 1) ? team1.selectHero(player, hero) : team2.selectHero(player, hero);
        }
        finally {
            locker.unlock();
        }
    }


    boolean isClosed() {
        locker.lock();

        try {
            return closed;
        } finally {
            locker.unlock();
        }
    }


    void setClosed() {
        locker.lock();
        this.closed = true;
        locker.unlock();
    }

    @Override
    public String toString() {
        return team1.toString() + "|" + team2.toString();
    }
}
