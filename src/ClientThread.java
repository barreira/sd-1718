import java.io.IOException;

public class ClientThread extends Thread {

    private String username;
    private String password;
    private String hero;

    public ClientThread(String username, String password, String hero) {
        this.username = username;
        this.password = password;
        this.hero = hero;
    }

    public void run() {
        try {
            OverwatchStub overwatch = new OverwatchStub("localhost", 2222);

//            overwatch.signup(username, password);
            Player player = overwatch.login(username, password);
//            System.out.println(player.getUsername() + " " + player.getPassword() + " " + player.getRanking() + " " + player.getVictories());

            Match m = overwatch.play();

            overwatch.selectHero(username, hero);

            overwatch.closeSocket();
        }
        catch (InvalidAccountException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
