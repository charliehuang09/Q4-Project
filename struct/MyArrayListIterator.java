package struct;

import java.util.Iterator;

public class MyArrayListIterator<E> implements Iterator<E> {
    private MyArrayList<E> list;
    private int i;

    public MyArrayListIterator(MyArrayList<E> list) {
        this.list = list;
        i = 0;
    }

    @Override
    public boolean hasNext() {
        return i < list.size();
    }

    @Override
    public E next() {
        return list.get(i++);
    }
}
