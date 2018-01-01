import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class PlayerQueue {

    private final List<String> queue;


    PlayerQueue() {
        queue = new ArrayList<>();
    }


    int size() {
        return queue.size();
    }

    void add(String p) {
        queue.add(p);
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

    public void remove(String username) {
        queue.remove(username);
    }
}
