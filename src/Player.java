import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Player implements Serializable {

    private String username;
    private String password;
    private int ranking;
    private int victories;

    final Lock locker;
    Condition hasMatch;

    /* Construtores */

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        ranking = 0;
        victories = 0;
        locker = new ReentrantLock();
        hasMatch = locker.newCondition();
    }

    public Player(String username, String password, int ranking, int victories) {
        this.username = username;
        this.password = password;
        this.ranking = ranking;
        this.victories = victories;
        locker = new ReentrantLock();
        hasMatch = locker.newCondition();
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