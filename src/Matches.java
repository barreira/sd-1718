import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


class Matches {

    private Map<Integer, Match> matches; // id da partida <-> lista de usernames dos players
    private int nextID;
    private final ReentrantLock locker;


    Matches() {
        matches = new HashMap<>();
        nextID = 0;
        locker = new ReentrantLock();
    }


    /*public boolean isPlaying(String username) {
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
    }*/


    Match getMatch(int id) {
        locker.lock();

        try {
            return matches.get(id);
        }
        finally {
            locker.unlock();
        }
    }


    /*public Match getPlayerMatch(String username) {
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
    }*/


//    public void addMatch(Match match) {
//        locker.lock();
//
//        try {
//            matches.put(nextID++, match);
//        }
//        finally {
//            locker.unlock();
//        }
//    }


    int addMatch(List<Player> players) {
        int rank = players.get(0).getRanking();
        List<Player> rank1 = new ArrayList<>();
        List<Player> rank2 = new ArrayList<>();
        List<String> t1 = new ArrayList<>();
        List<String> t2 = new ArrayList<>();
        boolean turn = false;

        for (Player p : players) {
            if (rank == p.getRanking()) {
                rank1.add(p);
            }
            else {
                rank2.add(p);
            }
        }

        for (Player p : rank1) {
            if (!turn) {
                t1.add(p.getUsername());
                turn = true;
            }
            else {
                t2.add(p.getUsername());
                turn = false;
            }
        }

        for (Player p : rank2) {
            if (!turn) {
                t1.add(p.getUsername());
                turn = true;
            }
            else {
                t2.add(p.getUsername());
                turn = false;
            }
        }


        locker.lock();

        try {
            matches.put(nextID, new Match(nextID, new Team(t1), new Team(t2)));
            return nextID++;
        }
        finally {
            locker.unlock();
        }
    }


    int getMatchID(String p) {
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


    void clearMatch(int matchID) {
        locker.lock();

        matches.remove(matchID);

        locker.unlock();
    }
}
