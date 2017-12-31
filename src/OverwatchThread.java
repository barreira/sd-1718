import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class OverwatchThread extends Thread {

    private Socket socket;
    private Player player;

    private Players players;
    private AvailablePlayers availablePlayers;
    private Matches matches;
    private Map<String, SharedCondition> shared;
//    private Connections connections;
//    private String fileName;


    public OverwatchThread(Socket socket, Player player, Players players, AvailablePlayers availablePlayers, Matches matches, Map<String, SharedCondition> shared) {
        this.socket = socket;
        this.player = player;
        this.players = players;
        this.availablePlayers = availablePlayers;
        this.matches = matches;
        this.shared = shared;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String line = null;
            String response = null;

            while ((line = in.readLine()) != null) {
                //System.out.println(line);

                String[] parts = line.split(":");

                if (parts[0].equals("signup")) {
                    try {
                        signup(parts[1], parts[2]);

                        response = "OK";
                    }
                    catch (InvalidAccountException e) {
                        response = "ERROR";
                    }
                }
                else if (parts[0].equals("login")) {
                    try {
                        player = login(parts[1], parts[2]);

                        response = "OK:" + player.getRanking() + ":" + player.getVictories();
                    }
                    catch (InvalidAccountException e) {
                        response = "ERROR";
                    }
                }
                else if (parts[0].equals("play")) {
                    try {
                        Match match = play();

                        response = "OK:" + match.toString();
                    }
                    catch (InterruptedException e) {
                        response = "ERROR";
                    }
                }

                out.println(response);
                out.flush();
            }

            socket.close();
//            overwatch.saveObject(fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player signup(String username, String password) throws InvalidAccountException {
        return players.signup(username, password);
    }

    public Player login(String username, String password) throws InvalidAccountException {
        Player p = players.login(username, password);

        SharedCondition sc = new SharedCondition();

        shared.put(username, sc);

        return p;
    }

    public Match play() throws InterruptedException {

        SharedCondition sc = shared.get(player.getUsername());
        sc.getLock();

        try {
            Match m = availablePlayers.addPlayer(player);

            if (m != null) {
                matches.addMatch(m);

                for (String username : m.playersInMatch()) {
                    if (!username.equals(player.getUsername())) {
                        shared.get(username).signalCond();
                    }
                }
            }

            while (!matches.isPlaying(player.getUsername())) {
                shared.get(player.getUsername()).waitCond();
            }

            return matches.getPlayerMatch(player.getUsername());
        }
        finally {
            shared.get(player.getUsername()).releaseLock();
        }
    }
}