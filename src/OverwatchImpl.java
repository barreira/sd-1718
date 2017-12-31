import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OverwatchImpl implements Overwatch {

    private Matches matches;
    private Players players;
    private AvailablePlayers availablePlayers;

    private final Lock locker;
    private Condition notInMatch;

    public OverwatchImpl() {
        matches = new Matches();
        players = new Players();
        availablePlayers = new AvailablePlayers();
        locker = new ReentrantLock();
        notInMatch = locker.newCondition();
    }

    public Player signup(String username, String password) throws InvalidAccountException {
        return players.signup(username, password);
    }

    public Player login(String username, String password) throws InvalidAccountException {
        return players.login(username, password);
    }

    public Match play(Player player) throws InterruptedException {
        locker.lock();
        player.locker.lock();

        try {
            Match m = availablePlayers.addPlayer(player);

            if (m != null) {
                matches.addMatch(m);
            }

            while (!matches.isPlaying(player.getUsername())) {
                notInMatch.await();
            }

            return matches.getPlayerMatch(player.getUsername());
        }
        finally {
            player.locker.unlock();
            locker.unlock();
        }

//            while (!matches.isPlaying(player.getUsername())) {
//                player.notInMatch.await();
//            }
    }

//    public synchronized void saveObject(String file) throws IOException {
//        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(file));
//
//        try {
//            oout.writeObject(this);
//        }
//        catch (IOException e) {
//            throw e;
//        }
//
//        oout.flush();
//        oout.close();
//    }
}