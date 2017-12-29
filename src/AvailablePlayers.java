import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AvailablePlayers {

    private Map<Integer, PlayerQueue> availablePlayers;
    private final Lock locker;
    private static final int NUM_PLAYERS = 4;

    public AvailablePlayers() {
        availablePlayers = new HashMap<>();
        locker = new ReentrantLock();

        for (int i = 0; i <= 9; i++) {
            availablePlayers.put(i, new PlayerQueue());
        }
    }

    public void addPlayer(Player player) throws InterruptedException {
        PlayerQueue queue, queueS, queueI;

        locker.lock();

        try {
            int rank = player.getRanking();

            queue = availablePlayers.get(rank);
            queueS = availablePlayers.get(rank + 1);
            queueI = availablePlayers.get(rank - 1);

            queue.locker.lock(); // este lock adquire o lock da PlayerQueue do rank em questão? isto é, se entretanto chegar um gajo com o mesmo rank, vai ter ficar à espera para obter as queues?
            queueS.locker.lock();
            queueI.locker.lock();
        }
        finally {
            locker.unlock();
        }

        try {
            queue.insertPlayer(player);

            while (queue.size() < NUM_PLAYERS && queue.size() + queueI.size() < NUM_PLAYERS
                                              && queue.size() + queueS.size() < NUM_PLAYERS) {
                player.hasMatch.await();
            }

            List<Player> playersInMatch;

            if (queue.size() >= NUM_PLAYERS) {
                playersInMatch = this.clearQueue(queue);
            }
            else if (queue.size() + queueI.size() >= NUM_PLAYERS) {
                playersInMatch = this.clearQueue(queue, queueI);
            }
            else if (queue.size() + queueS.size() >= NUM_PLAYERS) {
                playersInMatch = this.clearQueue(queue, queueS);
            }

            // dividir jogadores do jogo pelas duas equipas assegurando equilíbrio de ranks
            // criar dois objetos Team
            // com esses dois objetos, criar Match
            // devolver Match
            // (falta acordar restantes jogadores)
        }
        finally {
            locker.unlock();
        }
    }

    private List<Player> clearQueue(PlayerQueue queue) {
        return queue.clearQueue(NUM_PLAYERS);
    }

    private List<Player> clearQueue(PlayerQueue queue1, PlayerQueue queue2) {
        int s1 = queue1.size();
        int r = NUM_PLAYERS - s1;

        List<Player> players = queue1.clearQueue(s1);
        players.addAll(queue2.clearQueue(r));

        return players;
    }

    private class PlayerQueue {

        private List<Player> players;
        final ReentrantLock locker;

        private PlayerQueue() {
            players = new ArrayList<>();
            locker = new ReentrantLock();
        }

        private void insertPlayer(Player p) {
            players.add(p);
        }

        private int size() {
            return players.size();
        }

        private List<Player> clearQueue(int count) {
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
