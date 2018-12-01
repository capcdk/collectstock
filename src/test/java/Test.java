/**
 * Created by Chendk on 2018/10/10
 */
public class Test {

    static final ThreadLocal<String> localA = new ThreadLocal<>();
    ThreadLocal<String> localB = new ThreadLocal<>();

    public static void main(String[] args) {
        TestTA testTA = new TestTA();
        testTA.suspend();
    }

    static class TestTA extends Thread {
        @Override
        public void run() {
            localA.set("a");

            System.out.println("started");
            synchronized (localA) {
                System.out.println("entered");
                try {
                    localA.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class TestTB extends Thread {
        @Override
        public void run() {
            localA.set("a");

            System.out.println("started");
            synchronized (localA) {
                System.out.println("entered");
                try {
                    localA.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

