import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Players {

    private Map<String, Player> players;

    private final Lock locker;

    public Players() {
        players = new HashMap<>();
        locker = new ReentrantLock();
    }

    public Player signup(String username, String password) throws InvalidAccountException {
        locker.lock();

        try {
            Player p = players.get(username);

            if (p == null) {
                p = new Player(username, password);
                players.put(username, p);

                return p;
            }
            else {
                throw new InvalidAccountException();
            }
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
                    throw new InvalidAccountException();
                }
            }
            else { // Não existe player com username
                throw new InvalidAccountException();
            }
        }
        finally {
            locker.unlock();
        }
    }
}
