import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class OverwatchThread extends Thread {

    private Socket socket;
    private Player player;

    private Players players;
    private Matchmaking matchmaking;
    private Matches matches;
    private String fileName;
//    private Connections connections;


    public OverwatchThread(Socket socket, Player player, Players players, Matchmaking matchmaking, Matches matches) {
        this.socket = socket;
        this.player = player;
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
        return players.login(username, password);
    }

    public Match play() throws InterruptedException {
        int matchID = matchmaking.addPlayer(player, matches);

        return matches.getMatch(matchID);
    }
}