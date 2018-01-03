import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Notifications {

    private final Map<String, PrintWriter> clients;
    private final Lock locker;


    Notifications() {
        clients = new HashMap<>();
        locker = new ReentrantLock();
    }


    void addClient(String username, PrintWriter pw) {
        locker.lock();
        clients.put(username, pw);
        locker.unlock();
    }


    void notify(String username, String message) {
        locker.lock();

        PrintWriter pw = clients.get(username);

        locker.unlock();

        if (pw != null) {
            pw.println(message);
            pw.flush();
        }
    }


    void remove(String username) {
        locker.lock();
        clients.remove(username);
        locker.unlock();
    }
}
