import java.util.HashMap;

class Solution {
    public static void main(String[] args) {
        String lock1 = "lock1";
        String lock2 = "lock2";
        Runnable t1 = () -> {
            synchronized (lock1){
                System.out.println("t1 got lock1");
                synchronized (lock2){
                    System.out.println("t1 got lock2");
                }
            }
        };
    }
}