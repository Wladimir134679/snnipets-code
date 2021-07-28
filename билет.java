
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MainLauncher {

    public static int PORT = 13101;

    public static void main(String[] args) {
        System.out.println("Start app");
        long s = 0;
        int max = 9999_9999 + 1;
        for (int test = 0; test < 10; test++) {
            AtomicInteger numA = new AtomicInteger();
            AtomicInteger numB = new AtomicInteger();
            AtomicInteger numC = new AtomicInteger();
            long start = System.currentTimeMillis();
            IntStream.range(0, max)
                    .parallel()
                    .forEach(value -> {
                        int[] number = numberArray(value);
                        if (number[0] == number[1]) {
                            numA.getAndIncrement();
                        }
                        if (getSum(number[0]) == getSum(number[1])) {
                            numB.getAndIncrement();
                        }
                        if (isC(number)) {
                            numC.getAndIncrement();
                        }
                    });
            long end = System.currentTimeMillis();
            s += (end - start);
            System.out.println("================");
            System.out.println("Test: " + test);
            System.out.println("A = " + numA);
            System.out.println("B = " + numB);
            System.out.println("B% = " + (numB.get() / (float)max));
            System.out.println("C = " + numC);
            System.out.println("Time = " + (end - start));
        }
        System.out.println("S = " + (s / (float) 10));
    }

    private static boolean isC(int[] number) {
        return number[0] == Integer.parseInt(new StringBuilder(String.valueOf(number[1])).reverse().toString());
    }

    private static int getSum(int i) {
        int sum = 0;
        while (i != 0) {
            sum += i % 10;
            i /= 10;
        }
        return sum;
    }

    private static int[] numberArray(int i) {
        int a = i % 10000;
        int b = (i - a) / 10000;
        return new int[]{a, b};
    }
}
