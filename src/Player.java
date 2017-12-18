public class Player {

    private String username;
    private String password;
    private int ranking;
    private int victories;

    /* Construtores */

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        ranking = 0;
        victories = 0;
    }

    public Player(String username, String password, int ranking, int victories) {
        this.username = username;
        this.password = password;
        this.ranking = ranking;
        this.victories = victories;
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

    public int getVictories() {
        return victories;
    }

    public void addVictory() {
        victories++;
        ranking = (victories/10 <= 9) ? victories/10 : 9;
    }

    public void removeVictory() {
        if (victories > 0) {
            victories--;
        }

        ranking = (victories/10 <= 9) ? victories/10 : 9;
    }
}