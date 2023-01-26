# All-Primes
Using Java to spawn 8 threads to find all primes between 1 and 10^8 (0 and 1 don't count)

## How to compile and run program from the command prompt:
1. Pull this repo and cd'ing into your local directory using a terminal/command prompt
2. Enter the command `java AllPrimes.java` to both compile and run the program
3. Within that same directory, using your file navigator to open up the text file labeled `primes.txt`

## Approach
The first part of my approach was figuring out how to determine whether a number was prime or not. It took me several different phases before I settled on the ancient algorithm of [Sieve Of Eratosthenes](https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes). 
#### Dividing a N by every integer smaller than it in the interval [2, √(N)]
```
for (int i = 2; i <= Math.sqrt(n); i++) {
    if (n % i == 0) {
        // Here we acknowledge that n is not prime
    }
}
```
        
#### Dividing a N by every previous prime smaller than it in the interval [2, √(N)] 
``` 
Using memoization to create an ArrayList of primes that we've recently discovered as primes, starting with 2, 3, and 5. If there are no divisors that lead to no remainders, then we can add n to that memoized list of primes
```

#### Sieve Of Eratosthenes
``` 
(input: an integer n > 1, output: all prime numbers from 2 through n)

 let A be an array of Boolean values, indexed by integers 2 to n,
initially all set to true.

for i = 2, 3, 4, ..., not exceeding √n do
    if A[i] is true
        for j = i2, i2+i, i2+2i, i2+3i, ..., not exceeding n do
            set A[j] := false

return all i such that A[i] is true.
```
    
#### Using multiple threads

Here, we can start each thread at the first few numbers in the series of 2 to 10^8 and offset that number by the index/rank/number of each thread and increment by the amount of threads (in this case 8) so that they have roughly the same work done over time by having work done in the same ranges *without* interfering with other threads nor handling the same numbers

```
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
```

    
