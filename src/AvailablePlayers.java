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

    public Match addPlayer(Player player) throws InterruptedException {
        PlayerQueue queue, queueS, queueI;

        locker.lock();

        try {
            int rank = player.getRanking();

            queue = availablePlayers.get(rank);
            queueI = availablePlayers.get(rank - 1);
            queueS = availablePlayers.get(rank + 1);

            queue.locker.lock(); // este lock adquire o lock da PlayerQueue do rank em questão? isto é, se entretanto chegar um gajo com o mesmo rank, vai ter ficar à espera para obter as queues?
            queueI.locker.lock();
            queueS.locker.lock();
        }
        finally {
            locker.unlock();
        }

        try {
            queue.insertPlayer(player);

            int qSize = queue.size();
            int qISize = queueI.size();
            int qSSize = queueS.size();
            List<Player> playersInMatch = null;

            if (qSize >= NUM_PLAYERS) {
                playersInMatch = this.clearQueue(queue);
            }
            else if (qSize + qISize >= NUM_PLAYERS) {
                playersInMatch = this.clearQueue(queue, queueI);
            }
            else if (qSize + qSSize >= NUM_PLAYERS) {
                playersInMatch = this.clearQueue(queue, queueS);
            }
            else {
                while (qSize < NUM_PLAYERS && qSize + qISize < NUM_PLAYERS && qSize + qSSize < NUM_PLAYERS) { // while (true) ?
                    player.notInMatch.await();
                }
            }

            if (playersInMatch != null) { // o ultimo jogador a entrar cria o Match

                // dividir jogadores do jogo pelas duas equipas assegurando equilíbrio de ranks

                List<Player> t1 = playersInMatch.subList(0, 1);
                List<Player> t2 = playersInMatch.subList(2, 3);

                // criar dois objetos Team
                // com esses dois objetos, criar Match

                Match match = new Match(new Team(t1), new Team(t2));

                // acordar restantes jogadores

                for (Player p : playersInMatch) {
                    p.notInMatch.signal();
                }

                // devolver Match

                return match;
            }

            return null;
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

        List<Player> players = new ArrayList<>();

        players.addAll(queue1.clearQueue(s1));
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
