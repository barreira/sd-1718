import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


public class AvailablePlayers {

    private Map<Integer, PlayerQueue> availablePlayers;

    private final Lock locker;

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

            availablePlayers.get(rank).insertPlayer(player);

            queue = availablePlayers.get(rank);
            queueI = availablePlayers.get(rank - 1);
            queueS = availablePlayers.get(rank + 1);

            queue.locker.lock(); // este lock adquire o lock da PlayerQueue do rank em questão? isto é, se entretanto chegar um gajo com o mesmo rank, vai ter ficar à espera para obter as queues
            if (queueI != null) queueI.locker.lock();
            if (queueS != null) queueS.locker.lock();
        }
        finally {
            locker.unlock();
        }

        try {
            int qISize = 0, qSSize = 0;

            int qSize = queue.size();
            if (queueI != null) qISize = queueI.size();
            if (queueS != null) qSSize = queueS.size();
            List<Player> playersInMatch = null;

            if (qSize >= Overwatch.NUM_PLAYERS) {
                playersInMatch = this.clearQueue(queue);
            }
            else if (qSize + qISize >= Overwatch.NUM_PLAYERS && queueI != null) {
                playersInMatch = this.clearQueue(queue, queueI);
            }
            else if (qSize + qSSize >= Overwatch.NUM_PLAYERS && queueS != null) {
                playersInMatch = this.clearQueue(queue, queueS);
            }
//            else {
//                while (qSize < Overwatch.NUM_PLAYERS && qSize + qISize < Overwatch.NUM_PLAYERS && qSize + qSSize < Overwatch.NUM_PLAYERS) { // while (true) ?
//                    player.notInMatch.await();
//                }
//            }

            if (playersInMatch != null) { // o ultimo jogador a entrar cria o Match

                // dividir jogadores do jogo pelas duas equipas assegurando equilíbrio de ranks

                List<Player> t1 = playersInMatch.subList(0, 1);
                List<Player> t2 = playersInMatch.subList(2, 3);

                // criar dois objetos Team
                // com esses dois objetos, criar Match

                List<String> usernames1 = t1.stream().map(Player::getUsername).collect(Collectors.toList());
                List<String> usernames2 = t2.stream().map(Player::getUsername).collect(Collectors.toList());

                Match match = new Match(new Team(usernames1), new Team(usernames2));

                // acordar restantes jogadores

//                for (Player p : playersInMatch) {
//                    p.notInMatch.signal(); // não é preciso Condition
//                }

                // devolver Match

                return match;
            }

            return null;
        }
        finally {
            queue.locker.unlock();
            if (queueI != null) queueI.locker.lock();
            if (queueS != null) queueS.locker.lock();
        }
    }

    private List<Player> clearQueue(PlayerQueue queue) {
        return queue.clear(Overwatch.NUM_PLAYERS);
    }

    private List<Player> clearQueue(PlayerQueue queue1, PlayerQueue queue2) {
        int s1 = queue1.size();
        int r = Overwatch.NUM_PLAYERS - s1;

        List<Player> players = new ArrayList<>();

        players.addAll(queue1.clear(s1));
        players.addAll(queue2.clear(r));

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

        private List<Player> clear(int count) {
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
