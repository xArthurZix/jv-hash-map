package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_START_CAPASITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private float threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_START_CAPASITY];
        threshold = table.length * DEFAULT_LOAD_FACTOR;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Node<K, V> node = (Node<K, V>) obj;
            return (this == obj) || (obj != null && obj.equals(this));
        }

        @Override
        public int hashCode() {
            int hash = 17;
            hash = 31 * hash + key.hashCode();
            hash = 31 * hash + value.hashCode();
            return hash;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = hash(newNode.key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> oldNode = table[index];
        while (oldNode != null) {
            if (Objects.equals(oldNode.key, key)) {
                oldNode.value = value;
                return;
            }
            if (oldNode.next == null) {
                oldNode.next = newNode;
                size++;
                return;
            }
            oldNode = oldNode.next;
        }
    }

    private void resize() {
        threshold *= RESIZE_FACTOR;
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * RESIZE_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                if (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
