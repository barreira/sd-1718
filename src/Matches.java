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

//    public List<String> getPlayers(int id) {
//        locker.lock();
//
//        try {
//            return matches.get(id);
//        }
//        finally {
//            locker.unlock();
//        }
//    }
}
