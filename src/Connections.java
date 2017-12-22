import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class Connections {

    private Map<String, Connection> connections;

    private class Connection {

        private Socket socket;
        private PrintWriter printWriter;
    }
}
