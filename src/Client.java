import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        final int N = 4;

        Player[] jogadores = new Player[N];
        ClientThread[] threads = new ClientThread[N];

        threads[0] = new ClientThread("Ana", "123");
        threads[1] = new ClientThread("Bruno", "123");
        threads[2] = new ClientThread("Carlos", "123");
        threads[3] = new ClientThread("Daniela", "123");

        for (int i = 0; i < N; i++) {
            threads[i].start();
        }

        for (int i = 0; i < N; i++) {
            threads[i].join();
        }
    }
}