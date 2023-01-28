import java.io.*;
import java.util.*;
import java.lang.Math;

public class AllPrimes extends Thread {
    private static final int max = (int)10e7;
    private static final int numThreads = 8;
    private static final int topNumPrimes = 10;
    private static final String fileName = "primes.txt";

    // Stores true for an index whose integer is prime
    private static boolean[] isPrime = new boolean[max];
    private static int[] threadIterations = new int[numThreads];
    private static int count = max;
    private static long primeNumSum = summation(max);

    // Returns the summation of integers from 1 to N
    public static long summation(long n) {
        return (n*(n+1))/2;
    }

    // Thread call function
    public void run(int threadNum) {
        // Each thread skips an offset of 8 so that they do their part on each range, relatively splitting work
        for (int i = 2; i <= Math.sqrt(max); i++) {
            threadIterations[threadNum]++;
            if (isPrime[i] == true) {
                // Each thread starts at an offset element and skips by an offset to not interfere with another thread's work
                int startOffset = i*i + (i*threadNum);
                for (int j = startOffset; j < max; j += i*numThreads) {
                    if (isPrime[j] == true) {
                        isPrime[j] = false;
                        count--;
                        primeNumSum -= j;
                    }
                    threadIterations[threadNum]++;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        // By default, fill them with true and we'll set them otherwise manually
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;
        count -= 2; // To remove 0 and 1
        primeNumSum -= 1; // To remove 1 from the final prime sum
        AllPrimes[] threads = new AllPrimes[numThreads];

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new AllPrimes();
            // Telling which thread is who while running
            threads[i].run(i);
        }
        long endTime = System.currentTimeMillis();

        int[] topNumOfPrimes = new int[topNumPrimes];
        int topNumOfPrimesCounter = 0;
        String topNumOfPrimesString = "";
        int j;
        for (j = (isPrime.length-1); j > 2; j--) {
            if (isPrime[j]==true) {
                topNumOfPrimesString = topNumOfPrimesString + "\n" + j;
                topNumOfPrimesCounter++;
                if (topNumOfPrimesCounter > (topNumPrimes-1)) {
                    break;
                }
            }
        }

        String fileText = "Execution Time: " + (endTime - startTime) + "ms\t| Total Primes Found: "
                            + count + "\t| Sum of all Primes Found: " + primeNumSum
                            + "\nTop 10 Maximum Primes (From lowest to highest):" + topNumOfPrimesString
                            + "\nThread Iterations: " + Arrays.toString(threadIterations);

        File primesFile = new File(fileName);

        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(fileText);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
    }
}