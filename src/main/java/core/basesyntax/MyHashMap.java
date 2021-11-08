package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K, V>[] array;

    @Override
    public void put(K key, V value) {
        if (capacity == 0) {
            array = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
            capacity = INITIAL_CAPACITY;
            threshold = (int) (capacity * LOAD_FACTOR);
        }
        if (size + 1 > threshold) {
            resize();
        }
        int hash = (key == null) ? 0 : Math.abs(key.hashCode());
        int bucketIndex = hash % capacity;
        Node<K, V> node = array[bucketIndex];
        if (node == null) {
            array[bucketIndex] = new Node<>(hash, key, value, null);
            size = size + 1;
            return;
        }
        if (key == node.key || (key != null && key.equals(node.key))) {
            node.value = value;
            return;
        }
        while (node.next != null) {
            node = node.next;
            if (key == node.key || (key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
        }
        node.next = new Node<>(hash, key, value, null);
        size = size + 1;
    }

    @Override
    public V getValue(K key) {
        if (capacity == 0) {
            return null;
        }
        int bucketIndex = (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
        Node<K, V> node = array[bucketIndex];
        if (node == null) {
            return null;
        }
        do {
            if (key == node.key || (key != null && key.equals(node.key))) {
                return node.value;
            }
            node = node.next;
        } while (node != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity = capacity * 2;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] oldArray = array;
        size = 0;
        array = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
