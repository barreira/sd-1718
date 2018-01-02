
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


class Matchmaking {

    private static final int NUM_PLAYERS = 4;
    private static final int CENTRAL_INFERIOR = 0;
    private static final int CENTRAL = 1;
    private static final int CENTRAL_SUPERIOR = 2;

    private final Map<Integer, PlayerQueue> availablePlayers;
    private Map<String, WaitingPlayer> waitingPlayers;
    private final Players players;
    private final Notifications notifications;
    private final ReentrantLock locker;


    Matchmaking(Players players, Notifications notifications) {
        availablePlayers = new HashMap<>();
        waitingPlayers = new HashMap<>();
        this.players = players;
        this.notifications = notifications;
        locker = new ReentrantLock();

        for (int i = 0; i <= 9; i++) {
            availablePlayers.put(i, new PlayerQueue());
        }
    }


    int addPlayer(Player player, Matches matches) throws InterruptedException {
        PlayerQueue queue;
        PlayerQueue queueS;
        PlayerQueue queueI;
        int limit;
        int rank = player.getRanking();
        String p = player.getUsername();

        locker.lock();

        try {
            availablePlayers.get(rank).add(player);
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

            List<Player> playersInMatch;

            if (limit == CENTRAL) {
                playersInMatch = this.createMatch(queue, matches);
            }
            else if (limit == CENTRAL_INFERIOR) {
                playersInMatch = this.createMatch(queue, queueI, matches);
            }
            else {
                playersInMatch = this.createMatch(queue, queueS, matches);
            }

            // Acordar todos os jogadores que estão à espera
            for (Player s : playersInMatch) {
                if (!s.getUsername().equals(p)) {
                    waitingPlayers.get(s.getUsername()).signal();
                    waitingPlayers.remove(s.getUsername());
                }
            }

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


    private List<Player> createMatch(PlayerQueue queue, Matches matches) {
        List<Player> players = queue.remove(NUM_PLAYERS);

        int matchID = matches.addMatch(players);
        Timer t = new Timer(matchID, matches);
        t.start();

        return players;
    }


    private List<Player> createMatch(PlayerQueue queue1, PlayerQueue queue2, Matches matches) {
        int s1 = queue1.size();
        int r = NUM_PLAYERS - s1;

        List<Player> players = new ArrayList<>();

        players.addAll(queue1.remove(s1));
        players.addAll(queue2.remove(r));

        int matchID = matches.addMatch(players);

        Timer t = new Timer(matchID, matches);
        t.start();

        return players;
    }


    void clearPlayer(String username) {
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


    private class Timer extends Thread {

        private static final int TIME = 10000;

        private int matchID;
        private Matches matches;


        Timer(int matchID, Matches matches) {
            this.matchID = matchID;
            this.matches = matches;
        }


        public void run() {
            try {
                Thread.sleep(TIME);
                Match match = matches.getMatch(matchID);
                Random r = new Random();
                int winnerTeam = r.nextInt(2) + 1;

                match.setClosed();
                match.assignHeroes();

                for (String p : match.getTeam1().getPlayers()) {
                    notifications.notify(p, "START:" + match.toString());
                }

                for (String p : match.getTeam2().getPlayers()) {
                    notifications.notify(p, "START:" + match.toString());
                }

                if (winnerTeam == 1) {
                    for (String p : match.getTeam1().getPlayers()) {
                        notifications.notify(p, p + ":VICTORY!");
                        players.addVictory(p);
                    }

                    for (String p : match.getTeam2().getPlayers()) {
                        notifications.notify(p, p + ":DEFEAT...");
                        players.removeVictory(p);
                    }
                }
                else {
                    for (String p : match.getTeam2().getPlayers()) {
                        notifications.notify(p, p + ":VICTORY!");
                        players.addVictory(p);
                    }

                    for (String p : match.getTeam1().getPlayers()) {
                        notifications.notify(p, p + ":DEFEAT...");
                        players.removeVictory(p);
                    }
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
