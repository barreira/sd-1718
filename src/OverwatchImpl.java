import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class OverwatchImpl implements Overwatch, Serializable {

    private Map<String, Player> players;
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
}