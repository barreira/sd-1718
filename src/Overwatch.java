public interface Overwatch {

    public Player signup(String username, String password) throws InvalidAccountException;
    public Player login(String username, String password) throws InvalidAccountException;
    public Match play(Player player) throws InterruptedException;

    public final int NUM_PLAYERS = 4;
    static final String[] heroes = {"Ana", "Bastion", "D.Va", "Doomfist", "Genji", "Hanzo", "Junkrat", "Lúcio",
                                           "McCree", "Mei", "Mercy", "Moira", "Orisa", "Pharah", "Reaper", "Reinhardt",
                                           "Roadhog", "Soldier 76", "Sombra", "Symmetra", "Torbjorn", "Tracer",
                                           "Widowmaker", "Winston", "Zarya", "Zenyatta"};

    // public void selectHero(String hero);
}