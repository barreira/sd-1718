import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ConnectionHandler extends Thread {

    private Socket socket;
    private OverwatchImpl overwatch;
    private Player player;
//    private Connections connections;
//    private String fileName;

    public ConnectionHandler(Socket socket, OverwatchImpl overwatch, Player player) {
        this.socket = socket;
        this.overwatch = overwatch;
        this.player = player;
//        this.fileName = fileName;
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
                        overwatch.signup(parts[1], parts[2]);

                        response = "OK";
                    }
                    catch (InvalidAccountException e) {
                        response = "ERROR";
                    }
                }
                else if (parts[0].equals("login")) {
                    try {
                        player = overwatch.login(parts[1], parts[2]);

                        response = "OK:" + player.getRanking() + ":" + player.getVictories();
                    }
                    catch (InvalidAccountException e) {
                        response = "ERROR";
                    }
                }
                else if (parts[0].equals("play")) {
                    try {
                        Match match = overwatch.play(player);

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
}