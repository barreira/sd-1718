
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SharedCondition {
    
    
    //Variáveis de instância
    
    private ReentrantLock lock;
    private Condition notInMatch;

   
    //Construtores
    
    /**
     * Construtor vazio.
     */
    public SharedCondition() {
        lock = new ReentrantLock();
        notInMatch = lock.newCondition();
    }
    
    //Métodos de instância
    
    /**
     * Faz lock à variável lock da variável de condição.
     */
    public void getLock() {
        lock.lock();
    }
    
    /**
     * Faz unlock à variável lock da variável de condição.
     */
    public void releaseLock() {
        lock.unlock();
    }
    
    /**
     * Adormece a thread atual.
     * @throws InterruptedException 
     */
    public void waitCond() throws InterruptedException {
        notInMatch.await();
    }
    
    /**
     * Envia um sinal a uma thread adormecida para que ela acorde.
     */
    public void signalCond() {
        notInMatch.signal();
    }
}
