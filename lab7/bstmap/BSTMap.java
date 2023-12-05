package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left, right;

        private int children;

        private int leftWeight = 3;
        private int rightWeight = 5;




        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            children = 0;
        }
        public boolean hasOnechilren() {
            return (children == leftWeight || children == rightWeight);
        }
        public void removeChildnode(int weight) {
            if (weight == leftWeight) {
                left = null;
            } else if (weight == rightWeight) {
                right = null;
            } else if (weight == (rightWeight + leftWeight)) {
                left = null;
                right = null;
            }
        }

        public int findchildren(BSTNode child) {
            if (child.key == left.key) {
                return leftWeight;
            } else if (child.key == right.key) {
                return rightWeight;
            } else {
                return -1;
            }
        }

    }


    private BSTNode root;
    private int size;

    private Set<K> keyset;
    public BSTMap() {
        root = new BSTNode(null, null);
        keyset = new HashSet<>();
    }

    @Override
    public void clear() {
        root.key = null;
        root.value = null;
        root.right = null;
        root.left = null;

        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return helperContainsKey(root, key);
    }
    private boolean helperContainsKey(BSTNode node, K key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (node == null) {
            return false;
        }
        if (root.key == null) {
            return false;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return helperContainsKey(node.left, key);
        } else if (cmp > 0) {
            return helperContainsKey(node.right, key);
        } else {
            return true;
        }
    }

    @Override
    public V get(K key) {

        return helper(root, key);
    }

    private V helper(BSTNode node, K key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (node == null) {
            return null;
        }
        if (root.key == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return helper(node.left, key);
        } else if (cmp > 0) {
            return helper(node.right, key);
        } else {
            return node.value;
        }
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {


        findNodeToPut(root, key, value);
        size += 1;
        keyset.add(key);
    }

    /* This method is the helper method of put().
     * It helps to find the right node to put value.
     */
    private void findNodeToPut(BSTNode node, K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("puts an invalid key");
        }
        if (node.key == null) {
            node.key = key;
            node.value = value;
        } else {
            int cmp = key.compareTo(node.key);
            if ((cmp < 0) & node.left == null) {
                node.left = new BSTNode(key, value);
                node.children += node.leftWeight;

            } else if ((cmp > 0) & node.right == null) {
                node.right = new BSTNode(key, value);
                node.children += node.rightWeight;

            } else {
                if (cmp < 0) {
                    findNodeToPut(node.left, key, value);
                } else if (cmp > 0) {
                    findNodeToPut(node.right, key, value);
                } else {
                    //throw new IllegalArgumentException("There is already the same key");
                    node.value = value;
                }
            }
        }


    }

    public Set<K> keySet() {
        return keyset;
    }
    public V remove(K key) {
        if (containsKey(key)) {
            size -= 1;
            keyset.remove(key);
             return findNode(null, 0, root, key);
        } else {
            return null;
        }
    }

    /* this method helps to find the node to remove
    * @param weight illustrates the relationship between node and parent
    * */
    private V findNode(BSTNode parent, int weight, BSTNode node, K key) {
        if (key == null) {
            throw new IllegalArgumentException("invalid key");
        }
        if (node == null) {
            throw new IllegalArgumentException("the key you want to remove does not exist");
        }
        if (node.key == null) {
            return null;
        } else {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                return findNode(node, node.leftWeight, node.left, key);
            } else if (cmp > 0) {
                return findNode(node, node.rightWeight, node.right, key);
            } else {
                V returnvalue = node.value;
                if (parent != null) { //when the node to delete is root
                    helpRemove(parent, weight, node);
                } else {
                    if (root.children == 0) {
                        clear();
                    } else if (root.hasOnechilren()) {
                        if (root.left != null) {
                            root = root.left;
                        } else {
                            root = root.right;
                        }
                    } else {
                        helpRemove(parent, weight, node);
                    }
                }
                return returnvalue;
            }
        }

    }

    /*this method helps to remove the node*/
    private void helpRemove(BSTNode parent, int weight, BSTNode node) {
        if (node.children == 0) { // when the node to delete has no child
            if (weight == node.leftWeight) {
                parent.left = null;
            } else if (weight == node.rightWeight) {
                parent.right = null;
            }
        } else if (node.hasOnechilren()) { // when the node to delete has only 1 child
            BSTNode childnode;             //find the child of th node to be deleted
            if (node.children == node.leftWeight) {
                childnode = node.left;
            } else {
                childnode = node.right;
            }
            if (weight == parent.leftWeight) {      //point the parent of th node to the child of th node
                parent.left = childnode;
            } else if (weight == parent.rightWeight) {
                parent.right = childnode;
            }
        } else if (node.children == (node.leftWeight + node.rightWeight)) { // when the node to delete has 2 children.
            BSTNode nodeTochange = helpfindBiggestKey(null, node.left);
            node.key = nodeTochange.key;
            node.value = nodeTochange.value;
        }



    }

    private BSTNode helpfindBiggestKey(BSTNode parent, BSTNode node) {
        if (node.children == 0 || node.children == node.leftWeight) {
            if (parent != null) {
                parent.right = null;  //help to delete
            }
            return node;
        } else {
            return helpfindBiggestKey(node, node.right);
        }
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keyset.iterator();
    }


    public void printInOrder() {

    }
    public static void main(String[] args) {
        BSTMap<Integer, String> map = new BSTMap<>();
        map.put(1, "high");
        map.put(2, "high");
        map.put(3, "high");
        map.put(4, "high");
        System.out.println(map.containsKey(2));
        map.remove(4);
    }
}
