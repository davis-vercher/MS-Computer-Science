import java.util.concurrent.Semaphore;

class TeachingAssistant implements Runnable {
    private Semaphore chairs;
    private Semaphore available;

    public TeachingAssistant(Semaphore chairs, Semaphore available) {
        this.chairs = chairs;
        this.available = available;
    }

    @Override
    public void run() {
        while (true) { 
            try {
                System.out.println("TA is sleeping.");
                available.acquire(); 
                System.out.println("TA is helping a student.");
                Thread.sleep(3000); 
                System.out.println("TA has finished helping the student.");
                chairs.release(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

class Student implements Runnable {
    private int id;
    private Semaphore chairs;
    private Semaphore available;

    public Student(int id, Semaphore chairs, Semaphore available) {
        this.id = id;
        this.chairs = chairs;
        this.available = available;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (Math.random() * 10000)); 
                if (chairs.tryAcquire()) {
                    System.out.println("Student " + id + " is waiting for the TA.");
                    available.release(); 
                    chairs.acquire(); 
                    System.out.println("Student " + id + " is being helped by the TA.");
                    chairs.release(); 
                } else {
                    System.out.println("Student " + id + " is working without help as no chairs were available.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

public class SleepingTeachingAssistant {
    public static void main(String[] args) {
        int numberOfStudents = 5; 
        Semaphore chairs = new Semaphore(3);
        Semaphore available = new Semaphore(0); 

        Thread ta = new Thread(new TeachingAssistant(chairs, available));
        ta.start();

        for (int i = 0; i < numberOfStudents; i++) {
            Thread student = new Thread(new Student(i, chairs, available));
            student.start();
        }
    }
}
