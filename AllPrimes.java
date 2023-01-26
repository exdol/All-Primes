import java.io.*;
import java.util.*;
import java.lang.Math;

public class AllPrimes extends Thread {
    private static final int max = (int)10e7;
    private static final int numThreads = 8;
    private static final int topNumPrimes = 10;
    // Stores true for an index whose integer is prime
    private static boolean[] isPrime = new boolean[max];
    private static int[] threadIterations = new int[numThreads];
    private static final String fileName = "primes.txt";

    public void run(int threadNum) {
        // Each thread skips an offset of 8 so that they do their part on each range, relatively splitting work
        int i = (2+threadNum);
        while (i <= Math.sqrt(max)) {
            if (isPrime[i] == true) {
                for (int j = (i*i); j < max; j = j+i) {
                    isPrime[j] = false;
                    threadIterations[threadNum]++;
                }
            }
            i+= numThreads;
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int count = 0;
        long primeNumSum = 0;

        // By default, fill them with true and we'll set them otherwise manually
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;

        long startTime = System.currentTimeMillis();
        AllPrimes[] threads = new AllPrimes[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new AllPrimes();
            // Telling which thread is who while running
            threads[i].run(i);
        }
        long endTime = System.currentTimeMillis();

        int[] topNumOfPrimes = new int[topNumPrimes];
        int topNumOfPrimesCounter = 0;
        int j;
        for (j = (isPrime.length-1); j > 2; j--) {
            if (isPrime[j]==true) {
                count++;
                primeNumSum+= j;
                topNumOfPrimes[(topNumPrimes-1)-topNumOfPrimesCounter] = j;
                topNumOfPrimesCounter++;
                if (topNumOfPrimesCounter > (topNumPrimes-1)) {
                    break;
                }
            }
        }
        // Count how many numbers are still set as prime
        for (int i = (j-1); i > 1 ; i--) {
            if (isPrime[i]==true) {
                count++;
                primeNumSum+= i;
            }
        }

        String topNumOfPrimesString = "";
        for (int i = (topNumPrimes-1); i >= 0; i--) {
            topNumOfPrimesString = topNumOfPrimesString + "\n" + topNumOfPrimes[i];
        }

        File primesFile = new File(fileName);

        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("Execution Time: " + (endTime - startTime) + "ms\t| Total Primes Found: "
                            + count + "\t| Sum of all Primes Found: " + primeNumSum
                            + "\nTop 10 Maximum Primes (From lowest to highest):" + topNumOfPrimesString);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
    }
}