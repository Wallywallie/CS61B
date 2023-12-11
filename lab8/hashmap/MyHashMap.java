package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private Collection<Node> bucket;

    private double load;
    private int size;
    private int DEFAULTSIZE = 16;
    private double DEFAULTFACTOR = 0.75;

    private int keySize;
    private Set<K> keyset;

    /** Constructors */
    public MyHashMap() {
        size = DEFAULTSIZE;
        load = DEFAULTFACTOR;

        buckets = createTable(size);
        initializeTable(buckets);
        keySize = 0;
        keyset = new HashSet<>();



    }

    private void initializeTable(Collection<Node>[] table) {
        for (int i = 0; i < table.length; i += 1) {
            table[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        size = initialSize;
        bucket = createBucket();
        buckets = createTable(size);
        load = DEFAULTFACTOR;
        initializeTable(buckets);
        keySize = 0;
        keyset = new HashSet<>();
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size = initialSize;
        bucket = createBucket();
        buckets = createTable(size);
        load = maxLoad;
        initializeTable(buckets);
        keySize = 0;
        keyset = new HashSet<>();

    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {

        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    public void clear() {
        initializeTable(buckets);
        keySize = 0;
        keyset = new HashSet<>();
    }

    private int hash(K key, int mod) {
        int hashcode = key.hashCode();
        int returnvalue = Math.floorMod(hashcode, mod);
        return returnvalue;
    }

    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        int idx = hash(key, size);
        for (Node i : buckets[idx]) {
            if (i.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public V get(K key) {
        if (key == null) {
            return null;
        }
        int idx = hash(key, size);
        for (Node i : buckets[idx]) {
            if (i.key.equals(key)) {
                return i.value;
            }
        }
        return null;
    }

    public int size() {
        return keySize;
    }

    public void put(K key, V value) {
        if (key == null) {
            return;
        }
        if (size != 0) {
            double factor = keySize / (size * 1.0);
            if (factor > load) {
                resize(size * 2);
            }
        }
        int idx = hash(key, size);
        boolean hasKey = false;
        for (Node i : buckets[idx]) {
            if (i.key.equals(key)) {
                i.value = value;
                hasKey = true;
            }
        }
        if (!hasKey) {
            buckets[idx].add(createNode(key, value));
            keySize += 1;
            keyset.add(key);
        }


    }

    private void resize(int capacity) {
        Collection<Node>[] newbuckets = createTable(capacity);
        initializeTable(newbuckets);
        for (int i = 0; i < size; i += 1) {
            int newidx = Math.floorMod(i + size, capacity);
            newbuckets[newidx] = buckets[i];
        }
        size = capacity;
        buckets = newbuckets;
    }

    public Set<K> keySet() {
        return keyset;
    }

    public V remove(K key) {
        if (containsKey(key)) {
            int idx = hash(key, size);
            for (Node i : buckets[idx]) {
                if (i.key.equals(key)) {
                    V returnvalue = i.value;
                    buckets[idx].remove(i);
                    return returnvalue;
                }
            }
        }
        return null;
    }

    public V remove(K key, V value) {
        if (get(key) == value) {
            return remove(key);
        }
        return null;

    }

    public Iterator<K> iterator() {
        return keyset.iterator();
    }


}
