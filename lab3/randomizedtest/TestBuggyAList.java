package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {


  @Test
  public void testThreeAddThreeRemove() {
    AListNoResizing<Integer> test = new AListNoResizing<>();
    BuggyAList<Integer> bA = new BuggyAList<>();
    int[] arr = {4, 5, 6};
    for (int i = 0; i < arr.length; i += 1) {
      test.addLast(arr[i]);
      bA.addLast(arr[i]);
    }
    int[] arrExp = new int[test.size()];
    int[] arrAct = new int[bA.size()];

    for (int i = 0; i < bA.size(); i += 1) {
      arrExp[i] = test.getLast();
      test.removeLast();
      arrAct[i] = bA.getLast();
      bA.removeLast();
    }
    org.junit.Assert.assertArrayEquals(arrExp,arrAct);
  }
  @Test
  public void randomizedTest() {
    AListNoResizing<Integer> L = new AListNoResizing<>();
    BuggyAList<Integer> B = new BuggyAList<>();

    int N = 5000;
    int[] LsizeArr = new int[N];
    int[] BsizeArr = new int[N];

    for (int i = 0; i < N; i += 1) {
      int operationNumber = StdRandom.uniform(0, 4);
      if (operationNumber == 0) {
        // addLast
        int randVal = StdRandom.uniform(0, 100);
        L.addLast(randVal);
        B.addLast(randVal);

      } else if (operationNumber == 1) {
        // size
        int size = L.size();
        int Bsize = B.size();
        LsizeArr[i] = size;
        BsizeArr[i] =Bsize;
      } else if (operationNumber == 2) {
        //getLast
        if (L.size() > 0) {
          int last = L.getLast();
          int Blast = B.getLast();
          LsizeArr[i] = last;
          BsizeArr[i] = Blast;
        }
      } else if (operationNumber == 3) {
        //removeLast
        if (L.size() > 0) {
          L.removeLast();
          B.removeLast();
        }
      }
    }
    org.junit.Assert.assertArrayEquals(LsizeArr,BsizeArr);
  }

}
