import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

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

        players.add(new Player("Ana", "123", 1, 12));
        players.add(new Player("Bruno", "123", 1, 11));
        players.add(new Player("Carlos", "123", 0, 0));
        players.add(new Player("Daniela", "123", 1, 12));

        players.add(new Player("Elisio", "123", 0, 0));
        players.add(new Player("Francisco", "123", 0, 0));
        players.add(new Player("Guilherme", "123", 0, 0));
        players.add(new Player("Helder", "123", 1, 12));
        
        players.add(new Player("Ines", "123", 0, 0));
        players.add(new Player("Joao", "123", 0, 0));
        
        
        
        players.add(new Player("Kelly", "123", 4, 40));
        players.add(new Player("Luis", "123", 5, 52));
        
        players.add(new Player("Manuel", "123", 5, 50));
        players.add(new Player("Nuno", "123", 5, 54));
        players.add(new Player("Orlanda", "123", 5, 50));
        players.add(new Player("Paula", "123", 5, 52));
        
        players.add(new Player("Rafaela", "123", 4, 40));
        players.add(new Player("Sofia", "123", 5, 50));
        players.add(new Player("Tiara", "123", 5, 50));
        players.add(new Player("Zueiro", "123", 5, 52));

        return players;
    }
}