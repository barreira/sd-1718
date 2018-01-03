import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;


class OverwatchThread extends Thread {

    private static final String OK = "OK";
    private static final String GET = "get";
    private static final String DELIMITER = ":";
    private static final String ERROR = "ERROR";
    private static final String MATCH = "MATCH";
    private static final String ABORTED = "ABORTED";
    private static final String SIGNUP = "signup";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String PLAY = "play";
    private static final String HERO = "hero";


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
                String[] parts = line.split(DELIMITER);

                try {
                    switch (parts[0]) {
                        case SIGNUP:
                            player = signup(parts[1], parts[2]);
                            notifications.addClient(player.getUsername(), out);
                            response = OK;

                            break;

                        case LOGIN:
                            player = login(parts[1], parts[2]);
                            notifications.addClient(player.getUsername(), out);
                            response = OK + DELIMITER + player.getRanking() + DELIMITER + player.getVictories();

                            break;

                        case LOGOUT:
                            this.logout();
                            break;

                        case GET:
                            response = OK + DELIMITER + player.getRanking() + DELIMITER + player.getVictories();
                            break;
                            
                        case PLAY:
                            if (matchThread == null) {
                                matchThread = new MatchThread();
                                matchThread.start();
                            }

                            break;

                        case HERO:
                            if (match != null) {
                                if (!match.isClosed()) {
                                    boolean success = match.selectHero(player.getUsername(), parts[1]);

                                    if (success) {
                                        notifyMatch(OK + DELIMITER + player.getUsername() + DELIMITER + parts[1]);
                                    }
                                    else {
                                        response = ERROR + DELIMITER + player.getUsername() + DELIMITER + parts[1];
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
                    response = ERROR;
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

    
    private void notifyMatch(String message) {
        List<String> t1 = match.getTeam1().getPlayers();
        List<String> t2 = match.getTeam2().getPlayers();

        for (String p : t1) {
            notifications.notify(p, message);
        }

        for (String p : t2) {
            notifications.notify(p, message);
        }
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
                notifications.notify(p, ABORTED + DELIMITER + p);
            }

            for (String p : t2) {
                notifications.notify(p, ABORTED + DELIMITER + p);
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
                notifications.notify(player.getUsername(), MATCH + DELIMITER + match.toString());
            }
            catch (InterruptedException e) {
                // Nada a fazer
            }
        }
    }
}