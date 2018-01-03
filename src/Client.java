import java.io.IOException;


public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        final int N = 10;

        ClientThread[] threads = new ClientThread[N];

        threads[0] = new ClientThread("Guilherme", "123");
        threads[1] = new ClientThread("Zueiro", "123");
        threads[2] = new ClientThread("Tiara", "123");
        threads[3] = new ClientThread("Sofia", "123");
        threads[4] = new ClientThread("Rafaela", "123");
        
        
        threads[5] = new ClientThread("Ines", "123");
        threads[6] = new ClientThread("Joao", "123");
        threads[7] = new ClientThread("Elisio", "123");  
        threads[8] = new ClientThread("Paula", "123");
        threads[9] = new ClientThread("Francisco", "123");


        for (int i = 0; i < N; i++) {
            threads[i].start();
        }

        for (int i = 0; i < N; i++) {
            threads[i].join();
        }
    }
}