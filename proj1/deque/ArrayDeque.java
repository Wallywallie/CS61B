package deque;
import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
   T[] items;
   int size;
   int length;
   int zeroPos;
   double usageFac;

    public ArrayDeque() {
        length = 8;
        items = (T[]) new Object[length];
        size = 0;
        zeroPos = 0;
        usageFac = 0.25;

    }
    @Override
    public void addFirst(T item) {
        if (size == length) {
            resize(size * 2);
        }
        zeroPos = zeroPos - 1;
        if (zeroPos < -length) {
            zeroPos = zeroPos + length;
        }
        items[(length + zeroPos) % length] = item;
        size += 1;
    }
    @Override
    public void addLast(T item) {
        if (size == length) {
            resize(size * 2);
        }
        int endPos = zeroPos + size;
        items[(length + endPos) % length] = item;
        size += 1;

    }
    @Override
    public int size() {
        return size;
    }
    public T get(int index) {
        int actualIndex = (length + index + zeroPos) % length;
        return items[actualIndex];
    }
    @Override
    public void printDeque() {
        if (!this.isEmpty()) {
            for (int i = 0; i < size - 1; i += 1) {
                System.out.print(this.get(i) + " ");
            }
            System.out.print(this.get(size - 1));
            System.out.println();
        }
    }
    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        } else {
            if (length >= 16 && size <= length * usageFac) {
                resize(length / 2);
            }
            T returnValue = this.get(0);
            zeroPos += 1;
            size = size - 1;
            return returnValue;
            }
    }
    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        } else {
            if (length >= 16 && size <= length * usageFac) {
                resize(length / 2);
            }
            T returnValue = this.get(size - 1);
            size = size - 1;
            if (size == 0) {
                zeroPos = zeroPos + 1;
            }
            return returnValue;
        }
    }
    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];
        int endPos = zeroPos + size - 1;
        int copyStart = (zeroPos + length) % length;
        int copyEnd = (endPos + length) % length;

        if (copyStart > copyEnd) {
            int copysize = length - copyStart;
            System.arraycopy(items, copyStart, newArray, 0, copysize);
            System.arraycopy(items, 0, newArray, copysize, size - copysize);
        } else {
            int copysize2 = copyEnd - copyStart + 1;
            System.arraycopy(items, copyStart, newArray, 0, copysize2);
        }

        zeroPos = 0;
        items = newArray;
        length = capacity;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o instanceof Deque) {

            if (((Deque<T>) o).size() != size) {
                return false;
            } else {
                for (int i = 0; i < size; i += 1) {
                    if (((Deque<T>) o).get(i) != this.get(i)) {
                        return false;
                    }
                }
                return true;
                }

            } else {
            return false;
        }
    }
    @Override
    public Iterator<T> iterator() {
        return new generateIterator();
    }

    private class generateIterator implements Iterator<T>{
        int startIndex;
        public generateIterator() {
            startIndex = 0;

        }
        @Override
        public boolean hasNext() {
            if (startIndex < size) {
                return true;
            } else {
                return false;
            }
        }
        @Override
        public T next() {
            int iteratorIndex = (length + startIndex + zeroPos) % length;
            startIndex += 1;
            return items[iteratorIndex];
        }
    }
    public static void main (String[] args) {
        ArrayDeque<Integer> arr = new ArrayDeque<>();
        arr.addLast(1);
        arr.addLast(2);
        arr.addLast(3);
        arr.addFirst(0);
        arr.addFirst(-1);
        arr.printDeque();
        for (int i : arr) {
            System.out.println(i);
        }

    }
}


