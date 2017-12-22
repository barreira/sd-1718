import java.util.Map;

public class Players {

    private Map<String, Player> players;

    public synchronized Player signup(String username, String password) throws InvalidAccountException {
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

    public Player login(String username, String password) throws InvalidAccountException {
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
}
