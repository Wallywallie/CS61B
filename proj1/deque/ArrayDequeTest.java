package deque;

import org.junit.Test;

import java.util.Arrays;
import edu.princeton.cs.algs4.StdRandom;
import static org.junit.Assert.*;
public class ArrayDequeTest {

    @Test
    public void randomizedTest() {
        ArrayDeque<Integer> arr = new ArrayDeque<>();
        LinkedListDeque<Integer> deq = new LinkedListDeque<>();

        int N = 5000;
        int[] expectedArr = new int[N];
        int[] actualArr = new int[N];

        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                deq.addLast(randVal);
                arr.addLast(randVal);

            } else if (operationNumber == 1) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                deq.addFirst(randVal);
                arr.addFirst(randVal);

            } else if (operationNumber == 2) {
                //removeFirst
                if (deq.size() > 0) {
                    deq.removeFirst();
                    arr.removeFirst();
                }
            } else if (operationNumber == 3) {
                //removeLast
                if (deq.size() > 0) {
                    deq.removeLast();
                    arr.removeLast();
                }
            }
        }
        if (!deq.isEmpty()) {
            for (int i = 0; i < deq.size(); i += 1) {
                expectedArr[i] = deq.get(i);
                actualArr[i] = arr.get(i);
            }
        }
        org.junit.Assert.assertArrayEquals(expectedArr,actualArr);

    }



}



