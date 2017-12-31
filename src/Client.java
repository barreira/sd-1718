import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException, InvalidAccountException {

        OverwatchStub overwatch = new OverwatchStub("localhost", 4444);

        Player p = overwatch.signup("abc", "123");

        p = overwatch.login("abc", "123");

        System.out.println(p.getUsername() + p.getPassword() + p.getRanking() + p.getVictories());

        overwatch.closeSocket();
    }
}