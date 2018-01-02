import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        final int N = 4;

        ClientThread[] threads = new ClientThread[N];

        threads[0] = new ClientThread("Ana", "123", Overwatch.heroes[0]);
        threads[1] = new ClientThread("Bruno", "123", Overwatch.heroes[1]);
        threads[2] = new ClientThread("Carlos", "123", Overwatch.heroes[2]);
        threads[3] = new ClientThread("Daniela", "123", Overwatch.heroes[3]);
//        threads[4] = new ClientThread("Elisio", "123");
//        threads[5] = new ClientThread("Francisco", "123");
//        threads[6] = new ClientThread("Guilherme", "123");
//        threads[7] = new ClientThread("Helder", "123");

        for (int i = 0; i < N; i++) {
            threads[i].start();
        }

        for (int i = 0; i < N; i++) {
            threads[i].join();
        }
    }
}