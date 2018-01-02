import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {

    private final List<String> players;
    private final Map<String, String> selectedHeroes; // hero <-> player


    Team(List<String> players) {
        this.players = players;
        selectedHeroes = new HashMap<>();

        for (String p : players) {
            selectedHeroes.put(p, "");
        }
    }


    List<String> getPlayers() {
        return players;
    }


    Map<String, String> getSelectedHeroes() {
        return selectedHeroes;
    }


    boolean selectHero(String player, String hero) {
        for (String p : selectedHeroes.values()) {
            if (p.equals(hero)) {
                return false;
            }
        }

        selectedHeroes.put(player, hero);

        return true;
    }


//    public String getPlayerHero(String player) {
//        return selectedHeroes.get(player);
//    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String p : players) {
            sb.append(p);

            String hero = selectedHeroes.get(p);

            if (!hero.equals("")) {
                sb.append(",");
                sb.append(hero);
            }

            sb.append(":");
        }

        String s = sb.toString();

        return s.substring(0, s.length() - 1);
    }
}