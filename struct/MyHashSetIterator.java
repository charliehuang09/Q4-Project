package struct;
import java.util.Iterator;

public class MyHashSetIterator<E> implements Iterator<E> {
    private int currentIndex;
    private Object[] hashTable;

    public MyHashSetIterator(Object[] hashTable) {
        currentIndex = 0;
        this.hashTable = hashTable;
        findNext();
    }

    public void findNext() {
        currentIndex++;
        while (currentIndex < hashTable.length && hashTable[currentIndex] == null) {
            currentIndex++;
        }
    }

    @Override
    public boolean hasNext() {
        return currentIndex < hashTable.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        E obj = (E) hashTable[currentIndex];
        findNext();
        return obj;
    }
}