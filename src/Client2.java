import java.io.IOException;

public class Client2 {

    public static void main(String[] args) throws IOException, InterruptedException {

        final int N = 4;

        ClientThread[] threads = new ClientThread[N];

        threads[0] = new ClientThread("Guilherme", "123");
        threads[1] = new ClientThread("Helder", "123");
        threads[2] = new ClientThread("Elisio", "123");
        threads[3] = new ClientThread("Francisco", "123");

        for (int i = 0; i < N; i++) {
            threads[i].start();
        }

        for (int i = 0; i < N; i++) {
            threads[i].join();
        }
    }
}
