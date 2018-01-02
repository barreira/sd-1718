import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class PlayerQueue {

    private final List<Player> queue;


    PlayerQueue() {
        queue = new ArrayList<>();
    }


    int size() {
        return queue.size();
    }


    void add(Player p) {
        queue.add(p);
    }


    List<Player> remove(int count) {
        Iterator it = queue.iterator();
        List<Player> list = new ArrayList<>();
        int i = 0;

        while (it.hasNext() && i < count) {
            Player p = (Player)it.next();
            list.add(p);
            it.remove();
            i++;
        }

        return list;
    }

    void remove(String username) {
        Iterator it = queue.iterator();

        while (it.hasNext()) {
            Player p = (Player)it.next();

            if (p.getUsername().equals(username)) {
                it.remove();
                break;
            }
        }
    }
}
