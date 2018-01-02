import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {

    private List<String> players;
    private static Map<String, String> selectedHeroes; // hero <-> player

    public Team(List<String> players) {
        this.players = players;
        selectedHeroes = new HashMap<>();

        for (String p : players) {
            selectedHeroes.put(p, "");
        }
    }

    public List<String> getPlayers() {
        return players;
    }

    public Map<String, String> getSelectedHeroes() {
        return selectedHeroes;
    }

    public boolean selectHero(String player, String hero) {

        for (String p : selectedHeroes.values()) {
            if (p.equals(hero)) {
                return false;
            }
        }

        selectedHeroes.put(player, hero);

        return true;
    }

    public String getPlayerHero(String player) {
        return selectedHeroes.get(player);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String p : players) {
            sb.append(p);

            String hero = selectedHeroes.get(p);

            if (hero != null) {
                sb.append(",");
                sb.append(hero);
            }

            sb.append(":");
        }

        String s = sb.toString();

        return s.substring(0, s.length() - 1);
    }
}