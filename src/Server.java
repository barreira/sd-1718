import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static final String FILE = "overwatch.dat";

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(2222);

        Player player = new Player();
        Players players = Server.initApp();
//        Players players = new Players();
        Matchmaking matchmaking = new Matchmaking();
        Matches matches = new Matches();

        while (true) {
            Socket cs = ss.accept();

            OverwatchThread owt =  new OverwatchThread(cs, player, players, matchmaking, matches);

            owt.start();
        }
    }

    private static Players initApp() {
        Players players = new Players();

        players.add(new Player("Ana", "123", 2, 0));
        players.add(new Player("Bruno", "123", 2, 10));
        players.add(new Player("Carlos", "123", 2, 10));
        players.add(new Player("Daniela", "123", 2, 12));

        players.add(new Player("Elisio", "123", 0, 0));
        players.add(new Player("Francisco", "123", 0, 0));
        players.add(new Player("Guilherme", "123", 0, 0));
        players.add(new Player("Helder", "123", 1, 0));

        return players;

//        Players overwatch = null;
//
//        try {
//            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(FILE));
//            overwatch = (Players) oin.readObject();
//            oin.close();
//        }
//        catch (Exception e) {
//            overwatch = new Players();
//        }
//
//        return overwatch;
    }
}