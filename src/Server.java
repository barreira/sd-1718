import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

//    private static final String FILE = "overwatch.dat";

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(2222);

        Player player = new Player();
        Players players = Server.initApp();
        Notifications notifications = new Notifications();
        Matchmaking matchmaking = new Matchmaking(players, notifications);
        Matches matches = new Matches();

        while (true) {
            Socket cs = ss.accept();

            OverwatchThread owt =  new OverwatchThread(cs, player, players, matchmaking, matches, notifications);

            owt.start();
        }
    }

    private static Players initApp() {
        Players players = new Players();

        players.add(new Player("Ana", "123", 2, 22));
        players.add(new Player("Bruno", "123", 2, 21));
        players.add(new Player("Carlos", "123", 2, 20));
        players.add(new Player("Daniela", "123", 1, 12));

        players.add(new Player("Elisio", "123", 0, 0));
        players.add(new Player("Francisco", "123", 0, 0));
        players.add(new Player("Guilherme", "123", 0, 0));
        players.add(new Player("Helder", "123", 1, 12));

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