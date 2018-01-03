import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;


class OverwatchThread extends Thread {

    private Socket socket;
    private Player player;
    private Match match;

    private Players players;
    private Matchmaking matchmaking;
    private Matches matches;
    private Notifications notifications;
    private MatchThread matchThread;


    OverwatchThread(Socket socket, Player player, Players players, Matchmaking matchmaking, Matches matches,
                    Notifications notifications) {
        this.socket = socket;
        this.player = player;
        this.players = players;
        this.matchmaking = matchmaking;
        this.matches = matches;
        this.notifications = notifications;
        matchThread = null;
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
                            matchThread = new MatchThread();
                            matchThread.start();
                            break;

                        case "hero":
                            if (match != null) {
                                if (!match.isClosed()) {
                                    boolean success = match.selectHero(player.getUsername(), parts[1]);

                                    if (success) {
                                        response = "OK:" + player.getUsername() + ":" + parts[1];
                                    }
                                    else {
                                        response = "ERROR:" + player.getUsername() + ":" + parts[1];
                                    }
                                }
                                else {
                                    match = null;
                                }
                            }

                            break;

                        default:
                    }
                }
                catch (InvalidAccountException e) {
                    response = "ERROR";
                }

                if (response != null) {
                    notifications.notify(player.getUsername(), response);
                    response = null;
                }
            }


            this.clear();
            socket.close();
        }
        catch (IOException e) {
            this.clear();
        }
    }


    private Player signup(String username, String password) throws InvalidAccountException {
        return players.signup(username, password);
    }


    private Player login(String username, String password) throws InvalidAccountException {
        return players.login(username, password);
    }


    private void logout() {
        this.clear();
    }


    private void clear() {
        if (matchThread != null) {
            matchThread.interrupt();
            matchThread = null;
        }

        if (match != null) {
            List<String> t1 = match.getTeam1().getPlayers();
            List<String> t2 = match.getTeam2().getPlayers();

            for (String p : t1) {
                notifications.notify(p, "ABORTED:" + p);
            }

            for (String p : t2) {
                notifications.notify(p, "ABORTED:" + p);
            }

            matchmaking.abortMatch(match.getID());
            match = null;
        }

        matchmaking.clearPlayer(player.getUsername());
        notifications.remove(player.getUsername());
    }


    private class MatchThread extends Thread {

        @Override
        public void run() {
            try {
                int matchID = matchmaking.addPlayer(player, matches);

                match = matches.getMatch(matchID);
                notifications.notify(player.getUsername(), "MATCH:" + match.toString());
            }
            catch (InterruptedException e) {
                // Nada a fazer
            }
        }
    }
}