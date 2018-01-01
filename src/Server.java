import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

//    private static final String FILE = "overwatch.dat";

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(2222);
//        OverwatchImpl overwatch = Server.initApp();

        Player player = new Player();
        Players players = new Players();
        Matchmaking matchmaking = new Matchmaking();
        Matches matches = new Matches();

        while (true) {
            Socket cs = ss.accept();

            OverwatchThread owt =  new OverwatchThread(cs, player, players, matchmaking, matches);

            owt.start();
        }
    }

//    private static OverwatchImpl initApp() {
//
//        OverwatchImpl overwatch = null;
//
//        try {
//            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(FILE));
//            overwatch = (OverwatchImpl) oin.readObject();
//            oin.close();
//        }
//        catch (Exception e) {
//            overwatch = new OverwatchImpl();
//        }
//
//        return overwatch;
//    }
}