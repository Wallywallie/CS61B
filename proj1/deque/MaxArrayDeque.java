package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> cpt;
    public MaxArrayDeque(Comparator<T> c) {
        cpt = c;

    }
    public T max() {
        if (this.isEmpty()) {
            return null;
        } else {
            T maxEle = this.get(0);
            for (T i : this) {
                if (cpt.compare(maxEle, i) < 0) {
                    maxEle = i;
                }
            }
            return maxEle;
        }
    }
    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        } else {
            T maxEle = this.get(0);
            for (T i : this) {
                if (c.compare(maxEle, i) < 0) {
                    maxEle = i;
                }
            }
            return maxEle;
        }
    }


    }




