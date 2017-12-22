import java.util.concurrent.locks.ReentrantLock;

public class OverwatchSkeleton implements Overwatch {

    private Matches matches;
    private Players players;
    private AvailablePlayers availablePlayers;
    private Connections connections;

    private ReentrantLock locker;

    public OverwatchSkeleton() {
        locker = new ReentrantLock();
    }

    public Player signUp(String username, String password) throws InvalidAccountException {

    }

    public Player login(String username, String password) throws InvalidAccountException {

    }

    public Match play(String username) {

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