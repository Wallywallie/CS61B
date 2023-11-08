package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private IntNode sentinel;
    private int size;

    private class IntNode {
        private T item;
        private IntNode next;
        private IntNode prev;
        public IntNode(T i, IntNode nextN, IntNode prevN) {
            item = i;
            next = nextN;
            prev = prevN;
        }
    }

    public LinkedListDeque() {
        sentinel = new IntNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        sentinel.next.prev = new IntNode(item, sentinel.next, sentinel);
        sentinel.next = sentinel.next.prev;
        size += 1;
    }
    @Override
    public void addLast(T item) {
        sentinel.prev.next = new IntNode(item, sentinel, sentinel.prev);
        sentinel.prev = sentinel.prev.next;

        size += 1;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
        IntNode P = sentinel;

        for (int i = 0; i < size - 1; i += 1) {
            System.out.print(P.next.item + " ");
            P = P.next;
        }
        System.out.print(this.get(size - 1));
        System.out.println();
    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T result = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return result;
        }
    }
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T result = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size -= 1;
            return result;
        }
    }
    @Override
    public T get(int index) {
        IntNode p = sentinel;
        if (index < (size / 2) && index > -1) {
            for (int i = 0; i < index; i += 1) {
                p = p.next;
            }
            return p.next.item;
        } else if (index < size && index > -1) {
            int revIndex = size - index - 1;
            for (int i = 0; i < revIndex; i += 1) {
                p = p.prev;
            }
            return p.prev.item;
        } else {
            return null;
        }
    }

    public T getRecursive(int index) {
        IntNode p = sentinel;
        if (index < 0 || index >= size) {
            return null;
        } else {
            return helper(0, index, p);
        }
    }
    private T helper(int curIndex, int targetIndex, IntNode p) {
        if (curIndex == targetIndex) {
            return p.next.item;
        } else {
            int nextIndex = curIndex + 1;
            return helper(nextIndex, targetIndex, p.next);
        }
    }

    public Iterator<T> iterator() {
        return new GenerateIterator();
    }

    private class GenerateIterator implements Iterator {
        private int wizPoz;
        private IntNode p;

        public GenerateIterator() {
            wizPoz = 0;
            p = sentinel;
        }
        @Override
        public boolean hasNext() {
            return wizPoz < size;
        }
        @Override
        public T next() {
            T returnItem = p.next.item;
            p = p.next;
            wizPoz += 1;
            return returnItem;
        }
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
            if (((Deque<T>) o).size() == this.size()) {
                for (int i = 0; i < size; i += 1) {
                    if (!(((Deque<T>) o).get(i).equals(this.get(i)))) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
