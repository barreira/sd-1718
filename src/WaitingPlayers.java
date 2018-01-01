import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class WaitingPlayers {

    private Map<String, WaitingPlayer> waitingPlayers;
    private final ReentrantLock locker;


    public WaitingPlayers() {
        this.waitingPlayers = new HashMap<>();
        this.locker = new ReentrantLock();
    }


    void lock() {
        locker.lock();
    }

    void unlock() {
        locker.unlock();
    }

    public void waitPlayer(String p) throws InterruptedException {
        WaitingPlayer w = waitingPlayers.get(p);

        if (w == null) {
            w = new WaitingPlayer();
            waitingPlayers.put(p, w);
        }

        w.await();
    }

    public void signalPlayer(String p) {
        WaitingPlayer w = waitingPlayers.get(p);

        if (w != null) {
            w.signal();
        }
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
