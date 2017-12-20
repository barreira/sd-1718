import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException, InvalidAccountException {

        OverwatchStub overwatch = new OverwatchStub("localhost", 2222);

        //Player p = overwatch.signUp("abc", "123");

        Player p = overwatch.login("abc", "123");

        System.out.println(p.getUsername());

        overwatch.closeSocket();
    }
}