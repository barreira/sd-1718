public interface Overwatch {

    public Player signup(String username, String password) throws InvalidAccountException;
    public Player login(String username, String password) throws InvalidAccountException;
    public Match play(Player player) throws InterruptedException;

    public final int NUM_PLAYERS = 4;

    // public void selectHero(String hero);
}