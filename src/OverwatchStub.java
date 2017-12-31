import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class OverwatchStub {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String message;
    private String response;

    OverwatchStub(String host, int port) throws IOException {
        socket = new Socket(host, port);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public Player signup(String username, String password) throws InvalidAccountException {
        try {
            Player p = null;

            message = "signup:" + username + ":" + password;
            out.println(message);
            out.flush();

            response = in.readLine();
            //System.out.println(response);

            if (response.equals("OK")) {
                p = new Player(username, password);
            }
            else {
                throw new InvalidAccountException();
            }

            return p;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Player login(String username, String password) throws InvalidAccountException { // OK:ranking:victories
        try {
            Player p = null;

            message = "login:" + username + ":" + password;
            out.println(message);
            out.flush();

            response = in.readLine();
            String[] parts = response.split(":");

            if (parts[0].equals("OK")) {
                p = new Player(username, password, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            }
            else {
                throw new InvalidAccountException();
            }

            return p;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Match play(Player player) {
        try {
            message = "play";
            out.println(message);
            out.flush();

            response = in.readLine();
            String[] parts = response.split(":");

            if (parts[0].equals("OK")) {
                List<String> l = new ArrayList<>();

                for (int i = 1; i <= Overwatch.NUM_PLAYERS; i++) {
                    l.add(parts[i]);
                }

                Match m = new Match(new Team(l.subList(0, 1)), new Team(l.subList(2, 3)));

                return m;
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeSocket() throws IOException {
        socket.shutdownOutput();
    }
}