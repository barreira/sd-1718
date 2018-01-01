
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

class Matchmaking {

    private static final int NUM_PLAYERS = 4;
    private static final int CENTRAL_INFERIOR = 0;
    private static final int CENTRAL = 1;
    private static final int CENTRAL_SUPERIOR = 2;

    private final Map<Integer, PlayerQueue> availablePlayers;
    private Map<String, WaitingPlayer> waitingPlayers;
    private final ReentrantLock locker;


    Matchmaking() {
        availablePlayers = new HashMap<>();
        waitingPlayers = new HashMap<>();
        locker = new ReentrantLock();

        for (int i = 0; i <= 9; i++) {
            availablePlayers.put(i, new PlayerQueue());
        }
    }


    int addPlayer(Player player, Matches matches) throws InterruptedException {
        PlayerQueue queue;
        PlayerQueue queueS;
        PlayerQueue queueI;
        int limit = CENTRAL;
        int rank = player.getRanking();
        String p = player.getUsername();

        locker.lock();

        try {
            availablePlayers.get(rank).add(p);
            queue = availablePlayers.get(rank);
            queueI = availablePlayers.get(rank - 1);
            queueS = availablePlayers.get(rank + 1);

            // adquirir os locks das queues necessários e libertar o lock do map

            if (queue.size() >= NUM_PLAYERS) {
                limit = CENTRAL;
            }
            else if (queueI != null && (queueI.size() + queue.size() >= NUM_PLAYERS)) {
                limit = CENTRAL_INFERIOR;
            }
            else if (queueS != null && (queueS.size() + queue.size() >= NUM_PLAYERS)) {
                limit = CENTRAL_SUPERIOR;
            }
            else {
                // Neste caso liberta-se o lock do matchmakin

                // o jogador tem que esperar
                WaitingPlayer w = this.getWaitingPlayer(p);
                w.await();

                // Quando o jogador é acordado devolve logo a match dele
                return matches.getMatchID(p);
            }

            // Chegando aqui é sinal que o último jogador fez com que uma match seja válida
            // Liberta-se o lock do matchMacking primeiro

            List<String> playersInMatch = null;

            if (limit == CENTRAL) {
                playersInMatch = this.createMatch(queue, matches);
            }
            else if (limit == CENTRAL_INFERIOR) {
                playersInMatch = this.createMatch(queue, queueI, matches);
            }
            else {
                playersInMatch = this.createMatch(queue, queueS, matches);
            }


            System.out.println(p);
            System.out.println("antes " + waitingPlayers.keySet().toString());

            // Acordar todos os jogadores que estão à espera
            for (String s : playersInMatch) {
                if (!s.equals(p)) {
                    waitingPlayers.get(s).signal();
                    waitingPlayers.remove(s);
                }
            }

            System.out.println("depois " + waitingPlayers.keySet().toString());

            // Devolver o codigo da match
            return matches.getMatchID(p);
        }
        finally {
            locker.unlock();
        }
    }

    private WaitingPlayer getWaitingPlayer(String p) {
        WaitingPlayer w = waitingPlayers.get(p);

        if (w == null) {
            w = new WaitingPlayer();
            waitingPlayers.put(p, w);
        }

        return w;
    }

    private List<String> createMatch(PlayerQueue queue, Matches matches) {
        List<String> players = queue.remove(NUM_PLAYERS);

        matches.addMatch(players);

        return players;
    }

    private List<String> createMatch(PlayerQueue queue1, PlayerQueue queue2, Matches matches) {
        int s1 = queue1.size();
        int r = NUM_PLAYERS - s1;

        List<String> players = new ArrayList<>();

        players.addAll(queue1.remove(s1));
        players.addAll(queue2.remove(r));

        matches.addMatch(players);

        return players;
    }

    public void clearPlayer(String username) {
        locker.lock();

        for (PlayerQueue pq : availablePlayers.values()) {
            pq.remove(username);
        }
        waitingPlayers.remove(username);

        locker.unlock();
    }


    private class WaitingPlayer {

        private Condition condition;

        WaitingPlayer() {
            condition = locker.newCondition();
        }


        void await() throws InterruptedException {
            condition.await();
        }

        void signal() {
            condition.signal();
        }
    }
}
