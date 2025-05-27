package struct;
import java.util.Iterator;

public class MyHashSet<E> implements Iterable<E> {
    private Object[] hashTable;
    private int size;

    public MyHashSet() {
        hashTable = new Object[10000];
        size = 0;
    }

    public boolean add(E obj) {
        hashTable[getIndex(obj)] = obj;
        size++;
        return true;
    }

    public void clear() {
        hashTable = new Object[10000];
        size = 0;
    }

    public boolean contains(E obj) {
        return hashTable[getIndex(obj)] != null;
    }

    public boolean remove(E obj) {
        if (contains(obj)) {
            hashTable[getIndex(obj)] = null;
            size--;
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyHashSetIterator<E>(hashTable);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null) {
                sb.append(hashTable[i].toString());
                if (i < hashTable.length - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private int getIndex(E obj) {
        return (((obj.hashCode() % hashTable.length) + hashTable.length) % hashTable.length);
    }
}