package struct;

public class MyHashMap<K, V> {
    private Object[] hashArray;
    private int size;
    private MyHashSet<K> keySet;

    public MyHashMap() {
        hashArray = new Object[1000];
        size = 0;
        keySet = new MyHashSet<K>();
    }

    public V put(K key, V value) {
        int index = getIndex(key);

        keySet.add(key);

        V res = get(index);
        hashArray[index] = value;
        return res;
    }

    @SuppressWarnings("unchecked")
    public V get(Object key) {
        int index = getIndex(key);

        return (V) hashArray[index];
    }

    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        int index = getIndex(key);

        V res = get(key);
        hashArray[index] = null;
        keySet.remove((K) key);

        return res;
    }

    public int size() {
        return size;
    }

    public MyHashSet<K> keySet() {
        return keySet;
    }

    private int getIndex(Object key) {
        // use python modulus
        return (((key.hashCode() % 1000) + 1000) % 1000);
    }
}
