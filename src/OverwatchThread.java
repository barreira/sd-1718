import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class OverwatchThread extends Thread {

    private Socket socket;
    private Player player;
    private Match match;
    private boolean canSelect;

    private Players players;
    private Matchmaking matchmaking;
    private Matches matches;
    private String fileName;
//    private Connections connections;


    public OverwatchThread(Socket socket, Player player, Players players, Matchmaking matchmaking, Matches matches) {
        this.socket = socket;
        this.player = player;
        canSelect = true;
        this.players = players;
        this.matchmaking = matchmaking;
        this.matches = matches;
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

                try {
                    switch (parts[0]) {
                        case "signup":
                            signup(parts[1], parts[2]);

                            response = "OK";
                            break;

                        case "login":
                            player = login(parts[1], parts[2]);

                            response = "OK:" + player.getRanking() + ":" + player.getVictories();
                            break;

                        case "logout":
                            //
                            break;

                        case "play":
                            match = play();

                            response = "OK:" + match.toString();

                            Timer timer = new Timer();
                            timer.start();

                            break;

                        case "hero":
                            boolean success = match.selectHero(player.getUsername(), parts[1]);

                            if (success) {
                                response = "OK:" + parts[1];
                            }
                            else {
                                response = "ERROR:" + parts[1];
                            }
                            break;

                        default:
                    }
                }
                catch (InvalidAccountException | InterruptedException e) {
                    response = "ERROR";
                }

                out.println(response);
                out.flush();
            }

            socket.close();
//            players.saveObject(fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player signup(String username, String password) throws InvalidAccountException {
        return players.signup(username, password);
    }

    public Player login(String username, String password) throws InvalidAccountException {
        matchmaking.clearPlayer(username);
        matches.clearMatch(username);

        return players.login(username, password);
    }

    public void logout(String username) {
        matchmaking.clearPlayer(username);
        matches.clearMatch(username);
    }

    public Match play() throws InterruptedException {
        int matchID = matchmaking.addPlayer(player, matches);

        return matches.getMatch(matchID);
    }


    private class Timer extends Thread {

        private static final int TIME = 30000;

        private void assignHeroes(Map<String, String> team) {
            for (String p : team.keySet()) {
                if (team.get(p).equals("")) {

                }
            }
        }

        public void run() {
            try {
                Thread.sleep(TIME);
                canSelect = false;

                if (!canSelect) {
                    Map<String, String> team1 = match.selectedHeroes(1);
                    Map<String, String> team2 = match.selectedHeroes(2);

                    assignHeroes(team1);
                    assignHeroes(team2);
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}