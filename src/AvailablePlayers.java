import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class AvailablePlayers {

    private Map<Integer, PlayerQueue> availablePlayers;
    private final ReentrantLock locker;

    private static final int NUM_PLAYERS = 4;

    public AvailablePlayers() {
        availablePlayers = new HashMap<>();
        locker = new ReentrantLock();

        for (int i = 0; i <= 9; i++) {
            availablePlayers.put(i, new PlayerQueue());
        }
    }

    public void addPlayer(Player p) {
        PlayerQueue queue, queueS, queueI;

        locker.lock();

        try {
            int rank = p.getRanking();

            queue = availablePlayers.get(rank);
            queueS = availablePlayers.get(rank + 1);
            queueI = availablePlayers.get(rank - 1);

            queue.locker.lock();
            queue.insertPlayer(p);

            if (queueS != null) {
                queueS.locker.lock();
            }

            if (queueI != null) {
                queueI.locker.lock();
            }
        } finally {
            locker.unlock();
        }

        try {
            if (queue.size() >= NUM_PLAYERS) {
                this.clearQueue(queue);
            } else if (queueI != null && (queue.size() + queueI.size() >= NUM_PLAYERS)) {
                this.clearQueue(queue, queueI);
            } else if (queueS != null && (queue.size() + queueS.size() >= NUM_PLAYERS)) {
                this.clearQueue(queue, queueS);
            }
        } finally {
            queue.locker.unlock();

            if (queueS != null) {
                queueS.locker.unlock();
            }

            if (queueI != null) {
                queueI.locker.unlock();
            }
        }
    }

    private void clearQueue(PlayerQueue queue) {
        List<Player> players = queue.clearQueue(NUM_PLAYERS);


    }

    private void clearQueue(PlayerQueue queue1, PlayerQueue queue2) {
        int s1 = queue1.size();
        int r = NUM_PLAYERS - s1;

        List<Player> players = queue1.clearQueue(s1);
        players.addAll(queue2.clearQueue(r));


    }


    private class PlayerQueue {

        private List<Player> players;
        final ReentrantLock locker;

        public PlayerQueue() {
            players = new ArrayList<>();
            locker = new ReentrantLock();
        }

        public void insertPlayer(Player p) {
            players.add(p);
        }

        public int size() {
            return players.size();
        }

        public List<Player> clearQueue(int count) {
            Iterator it = players.iterator();
            List<Player> list = new ArrayList<>();
            int i = 0;

            while (it.hasNext() && i < count) {
                Player p = (Player) it.next();
                list.add(p);
                it.remove();
                i++;
            }

            return list;
        }
    }
}
