import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler extends Thread {

    private Socket socket;
    private Overwatch overwatch;

    public ConnectionHandler(Socket socket, Overwatch overwatch) {
        this.socket = socket;
        this.overwatch = overwatch;
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}