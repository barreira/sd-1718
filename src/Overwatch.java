public interface Overwatch {

    public Player signUp(String username, String password) throws InvalidAccountException;
    public Player login(String username, String password) throws InvalidAccountException;
    public Match play(String username);

    // public void selectHero(String hero);
}