
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
        return players.signup(username, password);
    }

    public Player login(String username, String password) throws InvalidAccountException {
        return players.login(username, password);
    }

    public Match play(Player player) throws InterruptedException {
        player.locker.lock();

        try {
            availablePlayers.addPlayer(player);

//            while (!matches.isPlaying(player.getUsername())) {
//                player.notInMatch.await();
//            }

            return null;
        }
        finally {
            player.locker.unlock();
        }
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