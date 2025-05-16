package struct;

import java.util.Iterator;
import java.util.Comparator;

public class MyArrayList<E> implements Iterable<E> {

    private Object[] elementData;
    private int s;

    public MyArrayList() {
        elementData = new Object[10];
        s = 0;
    }

    public boolean add(E e) {

        ensureSize(s + 1);

        elementData[s] = e;
        s++;

        return true;
    }

    public boolean add(int i, E e) {

        ensureSize(s + 1);

        System.arraycopy(elementData, i, elementData, i + 1, s - i);

        elementData[i] = e;
        s++;

        return true;
    }

    @SuppressWarnings("unchecked")
    public E get(int i) {
        return (E) elementData[i];
    }

    @SuppressWarnings("unchecked")
    public E remove(int i) {
        E res = (E) elementData[i];
        System.arraycopy(elementData, i + 1, elementData, i, s - i - 1);
        s--;
        return res;
    }

    public E remove(E e) {
        for (int i = 0; i < s; i++) {
            if (elementData[i].equals(e)) {
                return remove(i);
            }
        }
        return null;
    }

    public int indexOf(E e) {
        for (int i = 0; i < s; i++) {
            if (elementData[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    public void set(int i, E e) {
        elementData[i] = e;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < s; i++) {
            sb.append(elementData[i]);
            if (i < s - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public void shuffle() {
        for (int i = 0; i < s; i++) {
            int j = (int) (Math.random() * s);
            Object temp = elementData[i];
            elementData[i] = elementData[j];
            elementData[j] = temp;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sort() {
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s - i - 1; j++) {
                if (((Comparable) elementData[j]).compareTo(elementData[j + 1]) > 0) {
                    Object temp = elementData[j];
                    elementData[j] = elementData[j + 1];
                    elementData[j + 1] = temp;
                }
            }
        }
    }

    public void sort(Comparator<E> comparator) {
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s - i - 1; j++) {
                if (comparator.compare(get(j), get(j + 1)) > 0) {
                    Object temp = elementData[j];
                    elementData[j] = elementData[j + 1];
                    elementData[j + 1] = temp;
                }
            }
        }
    }

    public int size() {
        return s;
    }

    public void clear() {
        s = 0;
    }

    public void ensureSize(int minCapacity) {

        int oldCapacity = elementData.length;
        if (minCapacity > oldCapacity) {
            Object[] oldData = elementData;
            int newCapacity = (oldCapacity * 3) / 2 + 1;
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            elementData = new Object[newCapacity];
            System.arraycopy(oldData, 0, elementData, 0, s);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new MyArrayListIterator<>(this);
    }
}