import java.util.Scanner;
import java.util.concurrent.*;

public class MsgSender {

    private static final int TotalProcessors = 10;
    private static final BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        String msg = "Food is ready.";
        Scanner sc = new Scanner(System.in);

        ExecutorService e_S = Executors.newFixedThreadPool(TotalProcessors);
        for (int i = 0; i < TotalProcessors; i++) {
            e_S.submit(new Processor(i, msgQueue));
        }

        while (true) {
            System.out.println("Options:");
            System.out.println("1. Broadcast");
            System.out.println("2. At least n");
            System.out.println("3. At most n");
            System.out.println("4. Exit");
            System.out.print("Enter your choice:");
            int c = sc.nextInt();

            if (c == 4) {
                System.out.println("Exiting...");
                e_S.shutdownNow();
                break;
            }

            int n = 0;
            if (c == 2 || c == 3) {
                System.out.print("n : ");
                n = sc.nextInt();

                if (n < 1 || n > TotalProcessors) {
                    System.out.println("Invalid value.");
                    continue;
                }
            }

            switch (c) {
                case 1:
                    broadcast(TotalProcessors, msg);
                    break;
                case 2:
                    atLeastN(n, msg);
                    break;
                case 3:
                    atMostN(n, msg);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void broadcast(int t_p, String msg) {
        for (int i = 0; i < t_p; i++) {
            msgQueue.offer(msg);
        }
    }

    private static void atLeastN(int n, String msg) {
        for (int i = 0; i < n; i++) {
            msgQueue.offer(msg);
        }
    }

    private static void atMostN(int n, String msg) {
        for (int i = 0; i < n; i++) {
            msgQueue.offer(msg);
        }
    }

    static class Processor implements Runnable {
        private final int pId;
        private final BlockingQueue<String> queue;

        Processor(int pId, BlockingQueue<String> queue) {
            this.pId = pId;
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String msg = queue.take();
                    p_M(pId, msg);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void p_M(int pId, String msg) {
            System.out.println("Processor " + pId + " : " + msg);
        }
    }
}
