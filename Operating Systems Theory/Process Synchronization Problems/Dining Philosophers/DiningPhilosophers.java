import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

// Define the interface for actions of DiningServer
interface DiningServer {
    void takeForks(int philosopherNumber);
    void returnForks(int philosopherNumber);
}

public class DiningPhilosophers implements DiningServer {

    private final Lock[] forkLocks;
    private final Condition[] forkConditions;
    private final boolean[] forkAvailable;

    public DiningPhilosophers(int numPhilosophers) {
        forkLocks = new ReentrantLock[numPhilosophers];
        forkConditions = new Condition[numPhilosophers];
        forkAvailable = new boolean[numPhilosophers];

        for (int i = 0; i < numPhilosophers; i++) {
            forkLocks[i] = new ReentrantLock();
            forkConditions[i] = forkLocks[i].newCondition();
            forkAvailable[i] = true;
        }
    }

    public void takeForks(int philosopherNumber) {
        forkLocks[philosopherNumber].lock();
        try {
            while (!forkAvailable[philosopherNumber]) {
                forkConditions[philosopherNumber].await();
            }

            // Lock the next fork
            forkLocks[(philosopherNumber + 1) % forkLocks.length].lock();
            try {
                while (!forkAvailable[(philosopherNumber + 1) % forkLocks.length]) {
                    forkConditions[(philosopherNumber + 1) % forkLocks.length].await();
                }

                // Now that both forks are available, mark them as not available
                forkAvailable[philosopherNumber] = false;
                forkAvailable[(philosopherNumber + 1) % forkLocks.length] = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                forkLocks[(philosopherNumber + 1) % forkLocks.length].unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            forkLocks[philosopherNumber].unlock();
        }
    }

    public void returnForks(int philosopherNumber) {
        forkLocks[philosopherNumber].lock();
        forkLocks[(philosopherNumber + 1) % forkLocks.length].lock();

        try {
            forkAvailable[philosopherNumber] = true;
            forkConditions[philosopherNumber].signal();

            forkAvailable[(philosopherNumber + 1) % forkLocks.length] = true;
            forkConditions[(philosopherNumber + 1) % forkLocks.length].signal();
        } finally {
            forkLocks[philosopherNumber].unlock();
            forkLocks[(philosopherNumber + 1) % forkLocks.length].unlock();
        }
    }

    private class Philosopher implements Runnable {
        private final int philosopherNumber;

        Philosopher(int philosopherNumber) {
            this.philosopherNumber = philosopherNumber;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    think();
                    takeForks(philosopherNumber);
                    eat();
                    returnForks(philosopherNumber);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interruption status
                }
            }
        }

        private void think() throws InterruptedException {
            System.out.println("Philosopher " + philosopherNumber + " is thinking.");
            Thread.sleep((long) (Math.random() * 1000));
        }

        private void eat() throws InterruptedException {
            System.out.println("Philosopher " + philosopherNumber + " is eating.");
            Thread.sleep((long) (Math.random() * 1000));
        }
    }

    public static void main(String[] args) {
        int numPhilosophers = 5;
        DiningPhilosophers diningPhilosophers = new DiningPhilosophers(numPhilosophers);

        Thread[] philosophers = new Thread[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            philosophers[i] = new Thread(diningPhilosophers.new Philosopher(i));
            philosophers[i].start();
        }
    }
}
