package common;

public interface Overwatch {

    public void register(String username, String password);
    public void login(String username, String password);
    public void play();
    public void selectHero(String hero);
}