import java.io.IOException;

public class ClientThread extends Thread {

    private String username;
    private String password;

    public ClientThread(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void run() {
        try {
            OverwatchStub overwatch = new OverwatchStub("localhost", 2222);

            overwatch.signup(username, password);
            Player player = overwatch.login(username, password);
            System.out.println(player.getUsername() + " " + player.getPassword() + " " + player.getRanking() + " " + player.getVictories());

            Match m = overwatch.play(player);
            System.out.println(m.toString());

            overwatch.closeSocket();
        }
        catch (InvalidAccountException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
