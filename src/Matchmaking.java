import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


class Matchmaking {

    private static final int CENTRAL_INFERIOR = 0;
    private static final int CENTRAL = 1;
    private static final int CENTRAL_SUPERIOR = 2;
    private static final String DELIMITER = ":";
    private static final String START = "START";
    private static final String VICTORY = "VICTORY!";
    private static final String DEFEAT = "DEFEAT...";

    private final Map<Integer, PlayerQueue> availablePlayers;
    private Map<String, WaitingPlayer> waitingPlayers;
    private final Players players;
    private final Notifications notifications;
    private final ReentrantLock locker;
    private final Map<Integer, Timer> matchThreads;


    Matchmaking(Players players, Notifications notifications) {
        availablePlayers = new HashMap<>();
        waitingPlayers = new HashMap<>();
        this.players = players;
        this.notifications = notifications;
        locker = new ReentrantLock();
        this.matchThreads = new HashMap<>();

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

            if (queue.size() >= Overwatch.NUM_PLAYERS) {
                limit = CENTRAL;
            }
            else if (queueI != null && (queueI.size() + queue.size() >= Overwatch.NUM_PLAYERS)) {
                limit = CENTRAL_INFERIOR;
            }
            else if (queueS != null && (queueS.size() + queue.size() >= Overwatch.NUM_PLAYERS)) {
                limit = CENTRAL_SUPERIOR;
            }
            else {
                // Neste caso liberta-se o lock do matchmaking

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
                    WaitingPlayer w = waitingPlayers.get(s.getUsername());

                    if (w != null) {
                        w.signal();
                        waitingPlayers.remove(s.getUsername());
                    }
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
        List<Player> players = queue.remove(Overwatch.NUM_PLAYERS);

        int matchID = matches.addMatch(players);
        Timer t = new Timer(matchID, matches);
        t.start();

        matchThreads.put(matchID, t);

        return players;
    }


    private List<Player> createMatch(PlayerQueue queue1, PlayerQueue queue2, Matches matches) {
        int s1 = queue1.size();
        int r = Overwatch.NUM_PLAYERS - s1;

        List<Player> players = new ArrayList<>();

        players.addAll(queue1.remove(s1));
        players.addAll(queue2.remove(r));

        int matchID = matches.addMatch(players);

        Timer t = new Timer(matchID, matches);
        t.start();

        matchThreads.put(matchID, t);

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


    void abortMatch(int matchID) {
        locker.lock();

        Timer t = matchThreads.get(matchID);

        if (t != null) {
            t.interrupt();
        }

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


        private void notitfyMatchAndResult(Match match, int team, List<String> teamPlayers, int winnerTeam) {
            for (String p : teamPlayers) {
                notifications.notify(p, START + DELIMITER + match.toString());

                if (team == winnerTeam) {
                    notifications.notify(p, p + DELIMITER + VICTORY);
                    players.addVictory(p);
                }
                else {
                    notifications.notify(p, p + DELIMITER + DEFEAT);
                    players.removeVictory(p);
                }
            }
        }


        public void run() {
            try {
                Thread.sleep(TIME);
                Match match = matches.getMatch(matchID);
                Random r = new Random();
                int winnerTeam = r.nextInt(2) + 1;

                match.setClosed();
                match.assignHeroes();

                this.notitfyMatchAndResult(match, 1, match.getTeam1().getPlayers(), winnerTeam);
                this.notitfyMatchAndResult(match, 2, match.getTeam2().getPlayers(), winnerTeam);

                matches.clearMatch(matchID);
                matchThreads.remove(matchID);
            }
            catch (InterruptedException e) {
                matches.getMatch(matchID).setClosed();
                matchThreads.remove(matchID);
                matches.clearMatch(matchID);
            }
        }
    }
}
