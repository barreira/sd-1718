import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class OverwatchStub {

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


    Player signup(String username, String password) throws InvalidAccountException {
        try {
            Player p;

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


    Player login(String username, String password) throws InvalidAccountException { // OK:ranking:victories
        try {
            Player p;

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


    void logout() {
        message = "logout";
        out.println(message);
        out.flush();
    }


    String play() {
        try {
            message = "play";
            out.println(message);
            out.flush();

            response = in.readLine();

            if (response.contains("MATCH")) {
                System.out.println(response);
                //return this.selectHero();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }


    String selectHero() {
        Random r = new Random();
        boolean canSelect = true;

        try {
            while (true) {
                int h = r.nextInt(26);

                Thread.sleep(2500);

                if (canSelect) {
                    message = "hero:" + Overwatch.heroes[h];
                    out.println(message);
                    out.flush();
                }

                response = in.readLine();
                System.out.println(response);

                if (response.contains("START")) {
                    canSelect = false;
                }
                else if (response.contains("VICTORY") || response.contains("DEFEAT")) {
                    break;
                }
                else if (response.contains("ABORTED")) {
                    break;
                }
            }
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }


    void closeSocket() throws IOException {
        socket.shutdownOutput();
    }
}