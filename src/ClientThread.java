import java.io.IOException;


class ClientThread extends Thread {

    private String username;
    private String password;


    ClientThread(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public void run() {
        try {
            String response;
            OverwatchStub overwatch = new OverwatchStub("localhost", 2222);

            overwatch.login(username, password);

            //do {
                response = overwatch.play();
            //} while (!response.contains("DEFEAT") && !response.contains("VICTORY"));

            overwatch.selectHero();
            overwatch.logout();
            overwatch.closeSocket();
        }
        catch (InvalidAccountException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
