public interface Overwatch {

    public Player signUp(String username, String password) throws InvalidAccountException;
    public Player login(String username, String password) throws InvalidAccountException;

    // public void play();
    // public void selectHero(String hero);
}