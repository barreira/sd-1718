import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final String FILE = "overwatch.dat";

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(2222);
        OverwatchImpl overwatch = Server.initApp();

        while (true) {
            Socket cs = ss.accept();
            ConnectionHandler ch =  new ConnectionHandler(cs, overwatch, FILE);
            ch.start();
        }
    }

    private static OverwatchImpl initApp() {

        OverwatchImpl overwatch = null;

        try {
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(FILE));
            overwatch = (OverwatchImpl) oin.readObject();
            oin.close();
        }
        catch (Exception e) {
            overwatch = new OverwatchImpl();
        }

        return overwatch;
    }
}