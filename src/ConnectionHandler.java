import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionHandler extends Thread {

    private Socket socket;
    private OverwatchSkeleton overwatch;
    private String fileName;

    private Matches matches;
    private Players players;
    private AvailablePlayers availablePlayers;
    private Connections connections;
    private Player player;

    private final ReentrantLock locker;
    private Condition hasMatch;

    public ConnectionHandler(Socket socket, OverwatchSkeleton overwatch, String fileName) {
        this.socket = socket;
        this.overwatch = overwatch;
        this.fileName = fileName;

        locker = new ReentrantLock();
        hasMatch = locker.newCondition();
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
                        overwatch.signUp(parts[1], parts[2]);

                        response = "OK";
                    }
                    catch (InvalidAccountException e) {
                        response = "ERROR";
                    }
                }
                else if (parts[0].equals("login")) {
                    try {
                        Player p = overwatch.login(parts[1], parts[2]);

                        response = "OK:" + p.getRanking() + ":" + p.getVictories();
                    }
                    catch (InvalidAccountException e) {
                        response = "ERROR";
                    }
                }

                out.println(response);
                out.flush();
            }

            socket.close();
            overwatch.saveObject(fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player signUp(String username, String password) throws InvalidAccountException {

    }

    public Player login(String username, String password) throws InvalidAccountException {

    }

    public Match play() throws InterruptedException {
        availablePlayers.addPlayer(player);

        while (!matches.isPlaying(player.getUsername())) {
            hasMatch.await();
        }
    }
}