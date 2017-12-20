import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class OverwatchImpl implements Overwatch, Serializable {

    private Map<String, Player> players;
    private Map<Integer, PlayerQueue> availablePlayers;
    private ReentrantLock locker;

    public OverwatchImpl() {
        players = new HashMap<>();
        locker = new ReentrantLock();
    }

    public Player signUp(String username, String password) throws InvalidAccountException {
        locker.lock();

        try {
            Player p = players.get(username);

            if (p == null) {
                p = new Player(username, password);
                players.put(username, p);
            }

            return p;
        }
        finally {
            locker.unlock();
        }
    }

    public Player login(String username, String password) throws InvalidAccountException {
        locker.lock();

        try {
            Player p = players.get(username);

            if (p != null) {
                if (p.getPassword().equals(password)) {
                    return p;
                }
                else { // Password errada
                    throw new RuntimeException();
                }
            }
            else { // Não existe player com username
                throw new RuntimeException();
            }
        }
        finally {
            locker.unlock();
        }
    }

    public Match play(String username) {
        int rank = players.get(username).getRanking();
        PlayerQueue queue = availablePlayers.get(rank);

        // Verificar mesmo rank

        if (queue.players.size() == 4) {
            queue.notPlaying.signalAll();
            // Match m = ...
            // return m;
        }

        // Verificar rank + (rank+1)

        else {
            PlayerQueue queueUp = availablePlayers.get(rank + 1);

            if () {

            }

            // Verificar rank + (rank-1)

            else {
                PlayerQueue queueDown = availablePlayers.get(rank - 1);

                if () {

                }
            }
        }

        // await()...
    }

    public synchronized void saveObject(String file) throws IOException {
        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(file));

        try {
            oout.writeObject(this);
        }
        catch (IOException e) {
            throw e;
        }

        oout.flush();
        oout.close();
    }

    private class PlayerQueue {

        private List<Player> players;
        Condition notPlaying;

        public PlayerQueue() {
            players = new ArrayList<>();
            notPlaying = locker.newCondition();
        }
    }
}