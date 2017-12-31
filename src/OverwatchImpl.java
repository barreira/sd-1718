
public class OverwatchImpl implements Overwatch {

//    private Matches matches;
    private Players players;
    private AvailablePlayers availablePlayers;

    public OverwatchImpl() {
//        matches = new Matches();
        players = new Players();
        availablePlayers = new AvailablePlayers();
    }

    public Player signup(String username, String password) throws InvalidAccountException {
        Player player = players.signup(username, password);

        return player;
    }

    public Player login(String username, String password) throws InvalidAccountException {
        Player player = players.login(username, password);

        return player;
    }

    public Match play(Player player) throws InterruptedException {

        return availablePlayers.addPlayer(player);

//            while (!matches.isPlaying(player.getUsername())) {
//                player.notInMatch.await();
//            }
    }

//    public synchronized void saveObject(String file) throws IOException {
//        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(file));
//
//        try {
//            oout.writeObject(this);
//        }
//        catch (IOException e) {
//            throw e;
//        }
//
//        oout.flush();
//        oout.close();
//    }
}