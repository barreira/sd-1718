import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

class PlayerQueue {

    private final List<String> queue;
    private final ReentrantLock locker;


    PlayerQueue() {
        queue = new ArrayList<>();
        locker = new ReentrantLock();
    }


    void lock() {
        locker.lock();
    }


    void unlock() {
        locker.unlock();
    }


    int size() {
        try {
            this.lock();
            return queue.size();
        } finally {
            this.unlock();
        }
    }


    void add(String p) {
        this.lock();
        queue.add(p);
        this.unlock();
    }


    List<String> remove(int count) {
        Iterator it = queue.iterator();
        List<String> list = new ArrayList<>();
        int i = 0;

        while (it.hasNext() && i < count) {
            String p = (String)it.next();
            list.add(p);
            it.remove();
            i++;
        }

        return list;
    }
}
