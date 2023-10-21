package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) { timeAListConstruction(); }
    public static double calculateTime(int times) {
        AList<Integer> Test = new AList<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < times; i +=1) {
            Test.addLast(1);
        }
        double timeInSeconds = sw.elapsedTime();
        return timeInSeconds;
    }
    public static void timeAListConstruction() {
        AList<Integer> Ns = new AList<>();
        AList<Integer> opCounts = new AList<>();
        AList<Double> times = new AList<>();
        int[] N = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        for (int i = 0; i < N.length; i += 1) {
            Ns.addLast(N[i]);
            opCounts.addLast(N[i]);
            times.addLast(calculateTime(N[i]));
        }
        printTimingTable(Ns, times, opCounts);






    }

}
