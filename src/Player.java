public class Player {

    private String username;
    private String password;
    private int ranking;

    /* Construtores */

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        ranking = 0;
    }

    public Player(String username, String password, int ranking) {
        this.username = username;
        this.password = password;
        this.ranking = ranking;
    }

    /* Getters */

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getRanking() {
        return ranking;
    }
}
