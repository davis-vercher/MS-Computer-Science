class Buffer {
    private static final int BUFFER_SIZE = 5;
    private Integer[] buffer;
    private int count;
    private int in;
    private int out;

    public Buffer() {
        buffer = new Integer[BUFFER_SIZE];
        count = 0;
        in = 0;
        out = 0;
    }

    public synchronized void insert(int item) {
        while (count == BUFFER_SIZE) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        buffer[in] = item;
        in = (in + 1) % BUFFER_SIZE;
        count++;
        notifyAll();
    }

    public synchronized int remove() {
        while (count == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1;
            }
        }

        int item = buffer[out];
        out = (out + 1) % BUFFER_SIZE;
        count--;
        notifyAll();
        return item;
    }
}

class Producer implements Runnable {
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        java.util.Random rand = new java.util.Random();
        while (true) {
            try {
                Thread.sleep(rand.nextInt(1000));
                int item = rand.nextInt(100);
                buffer.insert(item);
                System.out.println("Producer produced " + item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

class Consumer implements Runnable {
    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(new java.util.Random().nextInt(1000));
                int item = buffer.remove();
                System.out.println("Consumer consumed " + item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        Thread producerThread = new Thread(new Producer(buffer));
        Thread consumerThread = new Thread(new Consumer(buffer));

        producerThread.start();
        consumerThread.start();
    }
}
