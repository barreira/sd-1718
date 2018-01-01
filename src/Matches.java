import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Matches {

    private Map<Integer, Match> matches; // id da partida <-> lista de usernames dos players
    private int nextID;
    private final ReentrantLock locker;

    public Matches() {
        matches = new HashMap<>();
        nextID = 0;
        locker = new ReentrantLock();
    }

    public boolean isPlaying(String username) {
        locker.lock();

        try {
            for (Match m : matches.values()) {
                for (String u : m.playersInMatch()) {
                    if (u.equals(username)) {
                        return true;
                    }
                }
            }

            return false;
        }
        finally {
            locker.unlock();
        }
    }


    public Match getMatch(int id) {
        try {
            locker.lock();
            return matches.get(id);
        } finally {
            locker.unlock();
        }
    }


    public Match getPlayerMatch(String username) {
        locker.lock();

        try {
            for (Match m : matches.values()) {
                for (String u : m.playersInMatch()) {
                    if (u.equals(username)) {
                        return m;
                    }
                }
            }

            throw new RuntimeException("Player is not in any match");
        }
        finally {
            locker.unlock();
        }
    }

    public void addMatch(Match match) {
        locker.lock();

        try {
            matches.put(nextID++, match);
        }
        finally {
            locker.unlock();
        }
    }


    void addMatch(List<String> players) {
        List<String> t1 = players.subList(0, 2);
        List<String> t2 = players.subList(2, 4);

        locker.lock();
        matches.put(nextID++, new Match(new Team(t1), new Team(t2)));
        locker.unlock();
    }


    public int getMatchID(String p) {
        int mID = -1;

        try {
            locker.lock();

            for (int id : matches.keySet()) {
                Match m = matches.get(id);

                if (m.getTeam1().getPlayers().contains(p) || m.getTeam2().getPlayers().contains(p)) {
                    mID = id;
                    break;
                }
            }

            return mID;
        } finally {
            locker.unlock();
        }
    }
}
