import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Random;


public class OverwatchStub extends Observable {

    private static final String OK = "OK";
    private static final String DELIMITER = ":";
    private static final String MATCH = "MATCH";
    private static final String ABORTED = "ABORTED";
    private static final String SIGNUP = "signup";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String PLAY = "play";
    private static final String HERO = "hero";
    private static final String START = "START";
    private static final String VICTORY = "VICTORY!";
    private static final String DEFEAT = "DEFEAT...";

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


    String getResponse() {
        return response;
    }

    
    Player signup(String username, String password) throws InvalidAccountException {
        try {
            Player p;

            message = SIGNUP + DELIMITER + username + DELIMITER + password;
            out.println(message);
            out.flush();

            response = in.readLine();
            //System.out.println(response);

            if (response.equals(OK)) {
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

    
    Player get(String username, String password) {
        try {
            Player p;
            
            message = "get:" + username;
            out.println(message);
            out.flush();
            
            response = in.readLine();
            String[] parts = response.split(DELIMITER);

            p = new Player(username, password, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            
            return p;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    

    Player login(String username, String password) throws InvalidAccountException { // OK:ranking:victories
        try {
            Player p;

            message = LOGIN + DELIMITER + username + DELIMITER + password;
            out.println(message);
            out.flush();

            response = in.readLine();
            String[] parts = response.split(DELIMITER);

            if (parts[0].equals(OK)) {
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
        message = LOGOUT;
        out.println(message);
        out.flush();
    }
    

    void listen() {
        try {
            while (true) {
                response = in.readLine();
            
                this.setChanged();
                this.notifyObservers();
            
               
                if (response.contains(VICTORY) || response.contains(DEFEAT)) {
                    break;
                }
                else if (response.contains(ABORTED)) {
                    break;
                }
            }
        } 
        catch (IOException e) {
            
        }
    }
    
    
    String play() {
        try {
            message = PLAY;
            out.println(message);
            out.flush();

            response = in.readLine();

            if (response.contains(MATCH)) {
                this.setChanged();
                this.notifyObservers();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    
    void selectHero(String hero) {
        message = HERO + DELIMITER + hero;
        out.println(message);
        out.flush();
    }

    String selectHero() {
        Random r = new Random();
        boolean canSelect = true;

        try {
            while (true) {
                int h = r.nextInt(26);

                Thread.sleep(5000);

                if (canSelect) {
                    message = HERO + DELIMITER + Overwatch.heroes[h];
                    out.println(message);
                    out.flush();
                }

                response = in.readLine();
                System.out.println(response);

                if (response.contains(START)) {
                    canSelect = false;
                }
                else if (response.contains(VICTORY) || response.contains(DEFEAT)) {
                    break;
                }
                else if (response.contains(ABORTED)) {
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