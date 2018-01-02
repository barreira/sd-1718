import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Player implements Serializable {

    private String username;
    private String password;
    private int ranking;
    private int victories;


    /* Construtores */

    Player() {
        username = "";
        password = "";
        ranking = 0;
        victories = 0;
    }


    Player(String username, String password) {
        this.username = username;
        this.password = password;
        ranking = 0;
        victories = 0;
    }


    Player(String username, String password, int ranking, int victories) {
        this.username = username;
        this.password = password;
        this.ranking = ranking;
        this.victories = victories;
    }

    /* Getters */

    public String getUsername() {
        return username;
    }


    String getPassword() {
        return password;
    }


    int getRanking() {
        return ranking;
    }


    int getVictories() {
        return victories;
    }


    void addVictory() {
        victories++;
        ranking = (victories / 10 <= 9) ? victories / 10 : 9;
    }


    void removeVictory() {
        if (victories > 0) {
            victories--;
        }

        ranking = (victories / 10 <= 9) ? victories / 10 : 9;
    }
}