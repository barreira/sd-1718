public interface Overwatch {

    public Player signup(String username, String password) throws InvalidAccountException;
    public Player login(String username, String password) throws InvalidAccountException;
    public Match play(Player player) throws InterruptedException;

    // public void selectHero(String hero);
}