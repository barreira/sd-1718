import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


class OverwatchThread extends Thread {

    private Socket socket;
    private Player player;
    private Match match;

    private Players players;
    private Matchmaking matchmaking;
    private Matches matches;
    private Notifications notifications;
    //private String fileName;
//    private Connections connections;


    OverwatchThread(Socket socket, Player player, Players players, Matchmaking matchmaking, Matches matches,
                    Notifications notifications) {
        this.socket = socket;
        this.player = player;
        this.players = players;
        this.matchmaking = matchmaking;
        this.matches = matches;
        this.notifications = notifications;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String line;
            String response = null;

            while ((line = in.readLine()) != null) {
                String[] parts = line.split(":");

                try {
                    switch (parts[0]) {
                        case "signup":
                            player = signup(parts[1], parts[2]);
                            notifications.addClient(player.getUsername(), out);
                            response = "OK";
                            break;

                        case "login":
                            player = login(parts[1], parts[2]);
                            notifications.addClient(player.getUsername(), out);
                            response = "OK:" + player.getRanking() + ":" + player.getVictories();
                            break;

                        case "logout":
                            this.logout();
                            break;

                        case "play":
                            match = play();

                            response = "OK:" + match.toString();

                            break;

                        case "hero":
                            if (!match.isClosed()) {
                                boolean success = match.selectHero(player.getUsername(), parts[1]);

                                if (success) {
                                    response = "OK:" + player.getUsername() + ":" + parts[1];
                                }
                                else {
                                    response = "ERROR:" + player.getUsername() + ":" + parts[1];
                                }
                            }

                            break;

                        default:
                    }
                }
                catch (InvalidAccountException | InterruptedException e) {
                    response = "ERROR";
                }

                notifications.notify(player.getUsername(), response);
            }

            socket.close();
//            players.saveObject(fileName);
        }
        catch (IOException e) {
            matchmaking.clearPlayer(player.getUsername());
            matches.clearMatch(player.getUsername());
            e.printStackTrace();
        }
    }


    private Player signup(String username, String password) throws InvalidAccountException {
        return players.signup(username, password);
    }


    private Player login(String username, String password) throws InvalidAccountException {
        return players.login(username, password);
    }


    private void logout() {
        matchmaking.clearPlayer(player.getUsername());
        matches.clearMatch(player.getUsername());
    }


    private Match play() throws InterruptedException {
        int matchID = matchmaking.addPlayer(player, matches);

        return matches.getMatch(matchID);
    }
}